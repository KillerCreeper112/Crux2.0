package killercreepr.cruxblocks.block.wrapper;

import killercreepr.crux.block.CruxBlockWrapper;
import killercreepr.crux.data.world.CruxPosition;
import killercreepr.cruxblocks.block.context.BlockContext;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class CruxGroupBlockWrapper implements CruxBlockWrapper {
    protected final @NotNull CruxBlockGroup group;
    public CruxGroupBlockWrapper(@NotNull CruxBlockGroup group) {
        this.group = group;
    }

    @Override
    public void setBlock(@NotNull World world, @NotNull CruxPosition position, boolean applyPhysics) {
        group.getBaseBlock().setBlock(BlockContext.context(position.getBlock(world), null), applyPhysics);
    }
}
