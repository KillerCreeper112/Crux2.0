package killercreepr.crux.core.math;

import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.util.CruxMath;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SimpleCruxLocation implements CruxLocation {
    protected final double x;
    protected final double y;
    protected final double z;
    protected final float yaw;
    protected final float pitch;

    public SimpleCruxLocation(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SimpleCruxLocation blockPos = (SimpleCruxLocation) obj;
        return x == blockPos.x && y == blockPos.y && z == blockPos.z && blockPos.yaw == yaw && blockPos.pitch == pitch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, yaw, pitch);
    }

    @Override
    public String toString() {
        return "SimpleCruxLocation{x=" + x + ", y=" + y + ", z=" + z + ", yaw=" + yaw + ", pitch=" + pitch + "}";
    }

    @Override
    public @NotNull CruxLocation subtract(double x, double y, double z) {
        return new SimpleCruxLocation(this.x - x, this.y - y, this.z - z, yaw, pitch);
    }

    @Override
    public @NotNull CruxLocation add(double x, double y, double z) {
        return new SimpleCruxLocation(this.x + x, this.y + y, this.z + z, yaw, pitch);
    }

    @Override
    public @NotNull CruxLocation rotateAroundX(@NotNull CruxPosition center, double angle) {
        double radians = Math.toRadians(angle);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);

        // Translate point to origin
        double translatedY = this.y - center.y();
        double translatedZ = this.z - center.z();

        // Apply rotation matrix
        double rotatedY = Math.round(translatedY * cosTheta - translatedZ * sinTheta);
        double rotatedZ = Math.round(translatedY * sinTheta + translatedZ * cosTheta);

        // Translate point back
        return new SimpleCruxLocation(this.x, rotatedY + center.blockY(), rotatedZ + center.blockZ(), yaw, pitch);
    }

    @Override
    public @NotNull CruxLocation rotateAroundY(@NotNull CruxPosition center, double angle) {
        double radians = Math.toRadians(angle*-1);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);

        // Translate point to origin
        double translatedX = this.x - center.x();
        double translatedZ = this.z - center.z();

        // Apply rotation matrix
        double rotatedX = Math.round(translatedX * cosTheta - translatedZ * sinTheta);
        double rotatedZ = Math.round(translatedX * sinTheta + translatedZ * cosTheta);

        // Translate point back
        return new SimpleCruxLocation(rotatedX + center.blockX(), this.y, rotatedZ + center.blockZ(), yaw, pitch);
    }

    @Override
    public @NotNull CruxLocation rotateAroundZ(@NotNull CruxPosition center, double angle) {
        double radians = Math.toRadians(angle);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);

        // Translate point to origin
        double translatedX = this.x - center.x();
        double translatedY = this.y - center.y();

        // Apply rotation matrix
        double rotatedX = Math.round(translatedX * cosTheta - translatedY * sinTheta);
        double rotatedY = Math.round(translatedX * sinTheta + translatedY * cosTheta);

        // Translate point back
        return new SimpleCruxLocation(rotatedX + center.blockX(), rotatedY + center.blockY(), this.z, yaw, pitch);
    }

    @Override
    public @NotNull Location toLocation(World world) {
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public int blockX() {
        return (int) x;
    }

    @Override
    public int blockY() {
        return (int) y;
    }

    @Override
    public int blockZ() {
        return (int) z;
    }

    @Override
    public float yaw() {
        return yaw;
    }

    @Override
    public float pitch() {
        return pitch;
    }
}
