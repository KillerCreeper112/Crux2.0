package killercreepr.cruxblocks.core.block.wrapper;

import killercreepr.crux.api.block.CruxBlockWrapper;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.cruxblocks.api.block.context.BlockContext;
import killercreepr.cruxblocks.api.block.group.CruxBlockGroup;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class CruxGroupBlockWrapper implements CruxBlockWrapper {
    protected final @NotNull CruxBlockGroup group;
    public CruxGroupBlockWrapper(@NotNull CruxBlockGroup group) {
        this.group = group;
    }

    public @NotNull CruxBlockGroup getGroup() {
        return group;
    }

    @Override
    public void setBlock(@NotNull World world, @NotNull CruxPosition position, boolean applyPhysics) {
        group.getBaseBlock().setBlock(BlockContext.context(position.getBlock(world), null), applyPhysics);
    }
}
