package killercreepr.crux.data;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public record BlockPos(int x, int y, int z) {
    public static @NotNull BlockPos at(int x, int y, int z){
        return new BlockPos(x, y, z);
    }
    public static @NotNull BlockPos from(@NotNull Block block){
        return new BlockPos(block.getX(), block.getY(), block.getZ());
    }
    public static @NotNull BlockPos from(@NotNull Location l){
        return new BlockPos(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }
    public static @NotNull BlockPos from(@NotNull Vector v){
        return new BlockPos(v.getBlockX(), v.getBlockY(), v.getBlockZ());
    }
}
