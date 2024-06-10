package killercreepr.cruxblocks.block;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public interface CruxBlockDirectional extends CruxBlock {
    @NotNull BlockFace getDirection();
}
