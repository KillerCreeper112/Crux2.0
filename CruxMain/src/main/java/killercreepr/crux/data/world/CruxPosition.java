package killercreepr.crux.data.world;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public interface CruxPosition {
    default @NotNull CruxPosition add(@NotNull CruxPosition pos){
        return add(pos.x(), pos.y(), pos.z());
    }

    default @NotNull CruxPosition subtract(@NotNull CruxPosition pos){
        return subtract(pos.x(), pos.y(), pos.z());
    }

    @NotNull CruxPosition subtract(double x, double y, double z);
    @NotNull CruxPosition add(double x, double y, double z);

    @NotNull CruxPosition rotateAroundX(@NotNull CruxPosition center, double angle);
    @NotNull CruxPosition rotateAroundY(@NotNull CruxPosition center, double angle);
    @NotNull CruxPosition rotateAroundZ(@NotNull CruxPosition center, double angle);

    double x();
    double y();
    double z();

    int blockX();
    int blockY();
    int blockZ();

    default @NotNull Block getBlock(@NotNull World world){
        return world.getBlockAt(blockX(), blockY(), blockZ());
    }

    default @NotNull Vector toVector(){
        return new Vector(x(), y(), z());
    }

    default @NotNull Location toLocation(World world){
        return new Location(world, x(), y(), z());
    }
}
