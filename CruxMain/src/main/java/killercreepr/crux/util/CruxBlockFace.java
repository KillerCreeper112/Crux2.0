package killercreepr.crux.util;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CruxBlockFace {
    private static final Map<BlockFace, BlockFace[]> rotationMap = Map.of(
        BlockFace.NORTH, new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST},
        BlockFace.EAST, new BlockFace[]{BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH},
        BlockFace.SOUTH, new BlockFace[]{BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST},
        BlockFace.WEST, new BlockFace[]{BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH},

        BlockFace.NORTH_EAST, new BlockFace[]{BlockFace.NORTH_EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST, BlockFace.NORTH_WEST},
        BlockFace.SOUTH_EAST, new BlockFace[]{BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST, BlockFace.NORTH_WEST, BlockFace.NORTH_EAST},
        BlockFace.SOUTH_WEST, new BlockFace[]{BlockFace.SOUTH_WEST, BlockFace.NORTH_WEST, BlockFace.NORTH_EAST, BlockFace.SOUTH_EAST},
        BlockFace.NORTH_WEST, new BlockFace[]{BlockFace.NORTH_WEST, BlockFace.NORTH_EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST}
    );

    public static BlockFace rotate(@NotNull BlockFace center, @NotNull BlockFace direction) {
        BlockFace[] rotations = rotationMap.get(center);
        if(rotations == null) throw new IllegalArgumentException("Unsupported center: " + center);
        int index = switch (direction){
            case NORTH -> 0;
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> throw new IllegalArgumentException("Unsupported direction: " + direction);
        };
        return rotations[index];
    }

    public static BlockFace rotateLeft(@NotNull BlockFace center) {
        return rotate(center, BlockFace.WEST);
    }

    public static BlockFace rotateRight(@NotNull BlockFace center) {
        return rotate(center, BlockFace.EAST);
    }
}
