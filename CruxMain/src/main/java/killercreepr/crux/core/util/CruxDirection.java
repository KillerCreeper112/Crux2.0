package killercreepr.crux.core.util;

import org.bukkit.Axis;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public class CruxDirection {
    public static @NotNull BlockFace getFaceFromAxis(@NotNull Axis direction){
        return switch (direction){
            case X -> BlockFace.EAST;
            case Y -> BlockFace.UP;
            case Z -> BlockFace.NORTH;
        };
    }
}
