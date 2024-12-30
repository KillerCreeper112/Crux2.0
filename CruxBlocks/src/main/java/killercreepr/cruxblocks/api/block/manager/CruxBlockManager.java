package killercreepr.cruxblocks.api.block.manager;

import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxBlockManager {
    @Nullable ActiveCruxBlock getActiveBlock(@NotNull Block at);

    /**
     *
     * @param at The current block.
     * @param data The block data to get the CruxBlock from.
     */
    @Nullable ActiveCruxBlock getActiveBlock(@NotNull Block at, @NotNull BlockData data);

    boolean hasActiveBlock(@NotNull Block at);

    @Nullable ActiveCruxBlock getActiveBlockPure(@NotNull Block at);
}
