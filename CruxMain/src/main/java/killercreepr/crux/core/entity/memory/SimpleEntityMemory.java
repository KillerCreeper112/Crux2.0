package killercreepr.crux.core.entity.memory;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.entity.memory.DataHolder;
import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.registry.KeyedRegistry;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleEntityMemory implements EntityMemory {
    protected final DataHolderRegistry dataHolders;
    protected final UUID uuid;
    protected final Holder<? extends Entity> entity;
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
        dataHolders.removeTickedIf(holder ->{
            if(holder.shouldRemoveFromMemory(e)){
                holder.removing(e);
                return true;
            }
            if(e != null) holder.tick(e);
            return false;
        });
        if(shouldRemoveFromMemory(e)){
            removeDataHolders(e);
            return true;
        }
        return false;
    }

    @Override
    public void onMemoryUnload(@NotNull Entity e) {
        for(DataHolder h : dataHolders.values()){
            h.onMemoryUnload(e);
        }
    }

    @Override
    public void removeDataHolders(@Nullable Entity e){
        for(DataHolder h : dataHolders.values()){
            h.parentRemoving(e);
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
