package killercreepr.crux.util;

import killercreepr.crux.data.world.CruxPosition;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class CruxWorldUtil {
    public static boolean isLoaded(@NotNull World world, int x, int z) {
        return world.isChunkLoaded(x >> 4, z >> 4);
    }

    public static boolean isLoaded(@NotNull World world, @NotNull Location loc) {
        return isLoaded(world, loc.getBlockX(), loc.getBlockZ());
    }

    public static boolean isLoaded(@NotNull World world, @NotNull CruxPosition loc) {
        return isLoaded(world, loc.blockX(), loc.blockZ());
    }

    public static boolean isLoaded(@NotNull World world, @NotNull Vector loc) {
        return isLoaded(world, loc.getBlockX(), loc.getBlockZ());
    }

    public static boolean isLoaded(@NotNull Location loc) {
        return loc.getWorld() != null && isLoaded(loc.getWorld(), loc);
    }

}
