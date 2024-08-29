package killercreepr.cruxblocks.block.wrapper;

import killercreepr.crux.block.CruxBlockWrapper;
import killercreepr.crux.data.world.CruxPosition;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.context.BlockContext;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class CruxBlockCruxWrapper implements CruxBlockWrapper {
    protected final @NotNull CruxBlock block;
    public CruxBlockCruxWrapper(@NotNull CruxBlock block) {
        this.block = block;
    }

    @Override
    public void setBlock(@NotNull World world, @NotNull CruxPosition position, boolean applyPhysics) {
        block.setBlock(BlockContext.context(position.getBlock(world), null), applyPhysics);
    }
}
