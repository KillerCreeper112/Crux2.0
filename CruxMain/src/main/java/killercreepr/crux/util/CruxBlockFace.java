package killercreepr.crux.util;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CruxBlockFace {
    private static final Map<BlockFace, BlockFace[]> rotationMap = new HashMap<>(){{
        put(BlockFace.NORTH, new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST});
        put(BlockFace.EAST, new BlockFace[]{BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH});
        put(BlockFace.SOUTH, new BlockFace[]{BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST});
        put(BlockFace.WEST, new BlockFace[]{BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH});
    }};

    public static BlockFace rotate(@NotNull BlockFace center, @NotNull BlockFace direction) {
        BlockFace[] rotations = rotationMap.get(center);
        return switch (direction) {
            case NORTH -> rotations[0];
            case EAST -> rotations[1];
            case SOUTH -> rotations[2];
            case WEST -> rotations[3];
            default -> throw new IllegalArgumentException("Unsupported direction: " + direction);
        };
    }

    public static BlockFace rotateLeft(@NotNull BlockFace center) {
        return rotate(center, BlockFace.WEST);
    }

    public static BlockFace rotateRight(@NotNull BlockFace center) {
        return rotate(center, BlockFace.EAST);
    }
}
