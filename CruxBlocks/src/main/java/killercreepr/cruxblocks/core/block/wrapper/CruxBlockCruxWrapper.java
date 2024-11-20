package killercreepr.cruxblocks.core.block.wrapper;

import killercreepr.crux.api.block.CruxBlockWrapper;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.context.BlockContext;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class CruxBlockCruxWrapper implements CruxBlockWrapper {
    protected final @NotNull CruxBlock block;
    public CruxBlockCruxWrapper(@NotNull CruxBlock block) {
        this.block = block;
    }

    public @NotNull CruxBlock getBlock() {
        return block;
    }

    @Override
    public void setBlock(@NotNull World world, @NotNull CruxPosition position, boolean applyPhysics) {
        block.setBlock(BlockContext.context(position.getBlock(world), null), applyPhysics);
    }
}
