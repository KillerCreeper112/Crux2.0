package killercreepr.cruxblocks.core.world.module;

import killercreepr.crux.api.data.tick.ManagedTicked;
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

import java.util.*;
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
    public void onLoad() {
        super.onLoad();

    }

    @Override
    public void onUnload(boolean save) {
        super.onUnload(save);
        active.values().forEach(b ->{
            if(!(b instanceof ManagedTicked ticked)) return;
            ticked.stopped();
        });
        active.clear();
        chunkToActive.clear();
    }

    @Override
    public void tick() {
        active.values().removeIf(a ->{
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
        });
    }

    @Override
    public void onChunkUnload(@NotNull Chunk chunk) {
        Collection<ActiveCruxBlock> active = this.chunkToActive.remove(chunk.getChunkKey());
        if(active == null) return;
        active.forEach(block ->{
            ActiveCruxBlock removed = this.active.remove(CruxPosition.block(block.getBlock()));
            if(!(removed instanceof ManagedTicked ticked)){
                Crux.log(Level.WARNING, block.getBlock() + " was supposed to be an active ticked block but was not registered properly!");
                return;
            }
            ticked.stopped();
        });
    }

    @Override
    public void onChunkLoad(@NotNull Chunk chunk) {
        CustomBlockData.getBlocksWithCustomData(chunk, ActiveCruxTickedBlock.CUSTOM_TICKED_KEY).forEach(this::getActiveBlock);
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
        if(hasActiveBlock(at)) return active.get(CruxPosition.block(at));
        CruxBlock block = blockRegistry.getByBlockData(at, data);
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
