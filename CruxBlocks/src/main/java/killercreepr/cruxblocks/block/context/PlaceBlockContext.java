package killercreepr.cruxblocks.block.context;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public interface PlaceBlockContext extends BlockContext{
    @NotNull
    BlockFace getBlockFace();
}
