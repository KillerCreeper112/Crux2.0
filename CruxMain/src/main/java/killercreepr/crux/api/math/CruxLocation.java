package killercreepr.crux.api.math;

import killercreepr.crux.core.math.SimpleCruxLocation;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface CruxLocation extends CruxPosition {
    static @NotNull CruxLocation location(double x, double y, double z, float yaw, float pitch){
        return new SimpleCruxLocation(x, y, z, yaw, pitch);
    }

    static @NotNull CruxLocation location(double x, double y, double z){
        return location(x, y, z, 0f, 0f);
    }

    static @NotNull CruxLocation location(@NotNull Block block){
        return location(block.getX(), block.getY(), block.getZ());
    }
    static @NotNull CruxLocation location(@NotNull Location l){
        return location(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
    }
    static @NotNull CruxLocation location(@NotNull Vector v){
        return location(v.getX(), v.getY(), v.getZ());
    }

    @Contract(pure = true)
    static CruxLocation getMaximum(@NotNull CruxLocation v1, @NotNull CruxLocation v2) {
        return location(Math.max(v1.x(), v2.x()), Math.max(v1.y(), v2.y()), Math.max(v1.z(), v2.z()), Math.max(v1.yaw(), v2.yaw()), Math.max(v1.pitch(), v2.pitch()));
    }
    @Contract(pure = true)
    static CruxLocation getMinimum(@NotNull CruxLocation v1, @NotNull CruxLocation v2) {
        return location(Math.min(v1.x(), v2.x()), Math.min(v1.y(), v2.y()), Math.min(v1.z(), v2.z()), Math.min(v1.yaw(), v2.yaw()), Math.min(v1.pitch(), v2.pitch()));
    }

    @Contract(pure = true)
    default @NotNull CruxLocation add(@NotNull CruxPosition pos){
        return add(pos.x(), pos.y(), pos.z());
    }
    @Contract(pure = true)
    default @NotNull CruxLocation subtract(@NotNull CruxPosition pos){
        return subtract(pos.x(), pos.y(), pos.z());
    }

    @Contract(pure = true)
    @NotNull CruxLocation subtract(double x, double y, double z);
    @Contract(pure = true)
    @NotNull CruxLocation add(double x, double y, double z);
    @Contract(pure = true)
    @NotNull CruxLocation rotateAroundX(@NotNull CruxPosition center, double angle);
    @Contract(pure = true)
    @NotNull CruxLocation rotateAroundY(@NotNull CruxPosition center, double angle);
    @Contract(pure = true)
    @NotNull CruxLocation rotateAroundZ(@NotNull CruxPosition center, double angle);

    @Contract(pure = true)
    @NotNull CruxLocation addRelative(double forward, double up, double right);
    @Contract(pure = true)
    @NotNull CruxLocation subtractRelative(double forward, double up, double right);

    @NotNull Location toLocation(World world);

    /*default CruxPosition getDirection(){
        Vector vector = new Vector();

        double rotX = this.getYaw();
        double rotY = this.getPitch();

        vector.setY(-Math.sin(Math.toRadians(rotY)));

        double xz = Math.cos(Math.toRadians(rotY));

        vector.setX(-xz * Math.sin(Math.toRadians(rotX)));
        vector.setZ(xz * Math.cos(Math.toRadians(rotX)));

        return vector;
    }*/

    @Contract(pure = true)
    @NotNull CruxLocation withYaw(float yaw);
    @Contract(pure = true)
    @NotNull CruxLocation withPitch(float pitch);
    @Contract(pure = true)
    @NotNull CruxLocation withX(double x);
    @Contract(pure = true)
    @NotNull CruxLocation withY(double y);
    @Contract(pure = true)
    @NotNull CruxLocation withZ(double z);

    float yaw();
    float pitch();
}
