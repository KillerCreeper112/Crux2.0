package killercreepr.cruxblocks.core.world.module;

import killercreepr.crux.api.data.tick.ManagedTicked;
import killercreepr.crux.api.entity.memory.TickedDataHolder;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.Crux;
import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.api.block.active.ActiveCruxTickedBlock;
import killercreepr.cruxblocks.api.block.registry.CruxBlockRegistry;
import killercreepr.cruxblocks.api.world.module.CruxBlocksWorldModule;
import killercreepr.cruxblocks.core.block.data.CustomBlockData;
import killercreepr.cruxworlds.api.world.CruxWorld;
import killercreepr.cruxworlds.core.world.module.SimpleWorldModule;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class SimpleCruxBlocksWorldModule extends SimpleWorldModule implements CruxBlocksWorldModule {
    protected final Map<CruxPosition, ActiveCruxBlock> active = new ConcurrentHashMap<>();
    protected final Map<Long, Collection<ActiveCruxBlock>> chunkToActive = new HashMap<>();
    protected final CruxBlockRegistry blockRegistry;
    public SimpleCruxBlocksWorldModule(@NotNull CruxWorld parent, CruxBlockRegistry blockRegistry) {
        super(parent);
        this.blockRegistry = blockRegistry;
    }

    @Override
    public void onUnload(boolean save) {
        super.onUnload(save);
        for (ActiveCruxBlock b : active.values()) {
            if(!(b instanceof ManagedTicked ticked)) continue;
            ticked.stopped();
        }
        active.clear();
        chunkToActive.clear();
    }

    protected ActiveCruxBlock[] removeBuffer = new ActiveCruxBlock[64];
    protected int removeCount = 0;
    protected void bufferRemove(){
        for (int i = 0; i < removeCount; i++) {
            active.remove(CruxPosition.block(removeBuffer[i].getBlock()));
            removeBuffer[i] = null;
        }
        removeCount = 0;
    }

    protected void addRemove(ActiveCruxBlock holder) {
        if (removeCount == removeBuffer.length) {
            ActiveCruxBlock[] newBuf =
                new ActiveCruxBlock[removeBuffer.length * 2];
            System.arraycopy(removeBuffer, 0, newBuf, 0, removeBuffer.length);
            removeBuffer = newBuf;
        }
        removeBuffer[removeCount++] = holder;
    }

    @Override
    public void tick() {
        removeCount = 0;
        for (ActiveCruxBlock a : active.values()) {
            if(!(a instanceof ManagedTicked t)){
                removeFromChunk(a);
                continue;
            }
            if(!a.isValid() || t.shouldStop() || !a.getBlock().getChunk().isLoaded()){
                t.stopped();
                removeFromChunk(a);
                addRemove(a);
                continue;
            }
            t.tick();
        }
        bufferRemove();

       /* active.values().removeIf(a ->{
            if(!(a instanceof ManagedTicked t)){
                removeFromChunk(a);
                return true;
            }
            if(!a.isValid() || t.shouldStop() || !a.getBlock().getChunk().isLoaded()){
                t.stopped();
                removeFromChunk(a);
                return true;
            }
            t.tick();
            return false;
        });*/
    }

    @Override
    public void onChunkUnload(@NotNull Chunk chunk) {
        Collection<ActiveCruxBlock> active = this.chunkToActive.remove(chunk.getChunkKey());
        if(active == null) return;
        for (ActiveCruxBlock block : active) {
            ActiveCruxBlock removed = this.active.remove(CruxPosition.block(block.getBlock()));
            if(!(removed instanceof ManagedTicked ticked)){
                Crux.log(Level.WARNING, block.getBlock() + " was supposed to be an active ticked block but was not registered properly!");
                continue;
            }
            ticked.stopped();
        }
        /*active.forEach(block ->{
            ActiveCruxBlock removed = this.active.remove(CruxPosition.block(block.getBlock()));
            if(!(removed instanceof ManagedTicked ticked)){
                Crux.log(Level.WARNING, block.getBlock() + " was supposed to be an active ticked block but was not registered properly!");
                return;
            }
            ticked.stopped();
        });*/
    }

    @Override
    public void onChunkLoad(@NotNull Chunk chunk) {
        CustomBlockData.forEachBlocksWithCustomData(chunk, ActiveCruxTickedBlock.CUSTOM_TICKED_KEY, this::getActiveBlock);
    }

    @Override
    public @Nullable Collection<ActiveCruxBlock> getActiveBlocks(long chunkKey) {
        return active.values();
    }

    @Override
    public @Nullable ActiveCruxBlock getActiveBlock(@NotNull Block at) {
        return getActiveBlock(at, at.getBlockData());
    }

    @Override
    public @Nullable ActiveCruxBlock getActiveBlock(@NotNull Block at, @NotNull BlockData data) {
        CruxBlock block = blockRegistry.getByBlockData(at, data);
        if(hasActiveBlock(at)){
            CruxPosition pos = CruxPosition.block(at);
            ActiveCruxBlock first = active.get(pos);
            if(first.getCruxBlock().compare(block)){
                return first;
            }
            removeActiveBlock(pos);
        }
        if(block==null) return null;
        ActiveCruxBlock active = block.createActive(at);

        if(active instanceof ManagedTicked ticked){
            addActiveBlock(active);
            ticked.started();
        }

        return active;
    }

    @Override
    public boolean hasActiveBlock(@NotNull Block at) {
        return active.containsKey(CruxPosition.block(at));
    }

    @Override
    public void addActiveBlock(@NotNull ActiveCruxBlock block) {
        this.active.put(CruxPosition.block(block.getBlock()), block);
        this.chunkToActive.computeIfAbsent(block.getBlock().getChunk().getChunkKey(), (l) -> new ArrayList<>()).add(block);
    }

    @Override
    public @Nullable ActiveCruxBlock getActiveBlockPure(@NotNull Block at) {
        return active.get(CruxPosition.block(at));
    }

    protected void removeFromChunk(ActiveCruxBlock b){
        chunkToActive.computeIfPresent(b.getBlock().getChunk().getChunkKey(), (key, list) ->{
            list.remove(b);
            return list.isEmpty() ? null : list;
        });
    }

    @Override
    public void clearActiveBlocks(long chunkKey) {
        Collection<ActiveCruxBlock> removed = chunkToActive.remove(chunkKey);
        if(removed != null){
            removed.forEach(active -> this.active.remove(CruxPosition.block(active.getBlock())));
        }
    }

    @Override
    public @Nullable ActiveCruxBlock removeActiveBlock(@NotNull CruxPosition pos) {
        ActiveCruxBlock removed = active.remove(pos);
        if(removed != null){
            removeFromChunk(removed);
        }
        return removed;
    }
}
