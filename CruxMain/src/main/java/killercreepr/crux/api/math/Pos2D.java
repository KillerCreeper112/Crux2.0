package killercreepr.crux.api.math;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public record Pos2D(int x, int z) {
    public static @NotNull Pos2D at(int x, int z){
        return new Pos2D(x, z);
    }
    public static @NotNull Pos2D at(@NotNull Block block){
        return at(block.getX(), block.getZ());
    }
    public static @NotNull Pos2D at(@NotNull Location l){
        return at(l.getBlockX(), l.getBlockZ());
    }
    public static @NotNull Pos2D at(@NotNull Vector l){
        return at(l.getBlockX(), l.getBlockZ());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + x;
        result = 31 * result + z;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Pos2D other = (Pos2D) obj;
        return x == other.x && z == other.z;
    }
}
