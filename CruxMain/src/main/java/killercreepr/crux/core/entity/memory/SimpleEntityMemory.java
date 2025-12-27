package killercreepr.crux.core.entity.memory;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.entity.memory.DataHolder;
import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.entity.memory.TickedDataHolder;
import killercreepr.crux.api.registry.KeyedRegistry;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleEntityMemory implements EntityMemory {

    protected final DataHolderRegistry dataHolders;
    protected final UUID uuid;
    protected final Holder<? extends Entity> entity;

    protected TickedDataHolder[] removeBuffer = new TickedDataHolder[16];
    protected int removeCount = 0;

    public SimpleEntityMemory(@NotNull Entity e, DataHolderRegistry dataHolders) {
        this(dataHolders, e.getUniqueId(), Holder.weakReference(e));
    }

    public SimpleEntityMemory(DataHolderRegistry dataHolders, @NotNull UUID uuid, @NotNull Holder<? extends Entity> holder){
        this.dataHolders = dataHolders;
        this.uuid = uuid;
        this.entity = holder;
    }

    public SimpleEntityMemory(@NotNull Entity e) {
        this(e, new DataHolderRegistry(new ConcurrentHashMap<>()));
    }

    public SimpleEntityMemory(@NotNull UUID uuid, @NotNull Holder<? extends Entity> holder){
        this(new DataHolderRegistry(new ConcurrentHashMap<>()), uuid, holder);
    }

    protected void bufferRemove(){
        for (int i = 0; i < removeCount; i++) {
            var got = removeBuffer[i];
            if(got == null) continue;
            dataHolders.unregister(got);
            removeBuffer[i] = null;
        }
        removeCount = 0;
    }

    protected void addRemove(TickedDataHolder holder) {
        if (removeCount == removeBuffer.length) {
            TickedDataHolder[] newBuf =
                new TickedDataHolder[removeBuffer.length * 2];
            System.arraycopy(removeBuffer, 0, newBuf, 0, removeBuffer.length);
            removeBuffer = newBuf;
        }
        removeBuffer[removeCount++] = holder;
    }

    @Override
    public @NotNull KeyedRegistry<DataHolder> getDataHolders() {
        return dataHolders;
    }

    @Override
    public @Nullable DataHolder getDataHolder(@NotNull Key key){
        return dataHolders.get(key);
    }

    @Override
    public @NotNull UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean tick(){
        Entity e = value();
        removeCount = 0;

        for (TickedDataHolder holder : dataHolders.getTickedHolders().values()) {
            if (holder.shouldRemoveFromMemory(e)) {
                holder.removing(e);
                addRemove(holder);
            } else if (e != null) {
                holder.tick(e);
            }
        }

        bufferRemove();

        if(shouldRemoveFromMemory(e)){
            removeDataHolders(e);
            return true;
        }
        return false;
    }

    @Override
    public void onMemoryUnload(@NotNull Entity e) {
        for(DataHolder h : dataHolders.values()){
            try{
                h.onMemoryUnload(e);
            }catch (Exception ignored){
                ignored.printStackTrace();
            }
        }
    }

    @Override
    public void removeDataHolders(@Nullable Entity e){
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for(DataHolder h : dataHolders.values()){
            if(!isAsync()){
                try{
                    h.parentRemoving(e);
                }catch (Exception ignored){
                    ignored.printStackTrace();;
                }
                return;
            }

            futures.add(CompletableFuture.runAsync(() ->{
                /*if(instantUnload){
                    try{
                        if(e != null) h.onMemoryUnload(e);
                    }catch (Exception ignored){
                        ignored.printStackTrace();
                    }
                }*/
                try{
                    h.parentRemoving(e);
                }catch (Exception ignored){
                    ignored.printStackTrace();;
                }
            }, CLEANUP_EXECUTOR));
        }
        if(futures.isEmpty()){
            dataHolders.clear();
            return;
        }

        CompletableFuture<Void> combined = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        combined.exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
        combined.whenCompleteAsync((unused, throwable) -> {
            EntityMemory.REMOVING_FUTURES.remove(uuid);
        });
        EntityMemory.REMOVING_FUTURES.put(uuid, combined);
        dataHolders.clear();
    }

    @Override
    public void forceRemoveDataHolders(@Nullable Entity e){
        for(DataHolder h : dataHolders.values()){
            try{
                if(e != null) h.onMemoryUnload(e);
            }catch (Exception ignored){
                ignored.printStackTrace();
            }
            try{
                h.parentRemoving(e);
            }catch (Exception ignored){
                ignored.printStackTrace();;
            }
        }
        dataHolders.clear();
    }

    protected boolean shouldRemoveFromMemory(@Nullable Entity e){
        return e == null || !e.isValid() || dataHolders.isEmpty();
    }

    @Nullable
    @Override
    public Entity value() {
        return entity.value();
    }
}
