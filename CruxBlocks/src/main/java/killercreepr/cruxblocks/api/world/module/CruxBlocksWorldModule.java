package killercreepr.cruxblocks.api.world.module;

import killercreepr.crux.api.data.Reloadable;
import killercreepr.crux.api.data.tick.Ticked;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import killercreepr.cruxworlds.api.world.module.WorldModule;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface CruxBlocksWorldModule extends WorldModule, Ticked, Reloadable {
    void onChunkUnload(@NotNull Chunk chunk);
    void onChunkLoad(@NotNull Chunk chunk);
    @Nullable
    Collection<ActiveCruxBlock> getActiveBlocks(long chunkKey);
    @Nullable
    ActiveCruxBlock getActiveBlock(@NotNull Block at);
    @Nullable ActiveCruxBlock getActiveBlock(@NotNull Block at, @NotNull BlockData data);
    boolean hasActiveBlock(@NotNull Block at);
    void addActiveBlock(@NotNull ActiveCruxBlock block);

    @Nullable ActiveCruxBlock getActiveBlockPure(@NotNull Block at);
    void clearActiveBlocks(long chunkKey);
    @Nullable ActiveCruxBlock removeActiveBlock(@NotNull CruxPosition pos);
}
