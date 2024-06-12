package killercreepr.cruxblocks.manager;

import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
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

    boolean hasTickedBlock(@NotNull Block at);

    @Nullable ActiveCruxBlock getTickedBlock(@NotNull Block at);
}
