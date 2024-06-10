package killercreepr.cruxblocks.block;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface CruxBlockDirectional extends CruxBlock {
    @NotNull Set<BlockFace> getFaces();
    BlockFace getFace(float pitch, BlockFace face);
    @NotNull BlockFace getDirection();
}
