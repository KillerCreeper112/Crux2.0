package killercreepr.crux.data;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BlockPos {
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

    protected final int x;
    protected final int y;
    protected final int z;
    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int z() {
        return z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BlockPos blockPos = (BlockPos) obj;
        return x == blockPos.x && y == blockPos.y && z == blockPos.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
