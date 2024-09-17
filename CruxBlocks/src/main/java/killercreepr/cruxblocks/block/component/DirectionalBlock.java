package killercreepr.cruxblocks.block.component;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public interface DirectionalBlock {
    @NotNull BlockFace getDirection();
}
