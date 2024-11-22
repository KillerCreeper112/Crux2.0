package killercreepr.crux.core.math;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.util.CruxMath;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LocationPos implements CruxPosition {
    public static @NotNull LocationPos at(double x, double y, double z){
        return new LocationPos(x, y, z);
    }
    @Deprecated(forRemoval = true)
    public static @NotNull LocationPos from(@NotNull Block block){
        return new LocationPos(block.getX(), block.getY(), block.getZ());
    }
    @Deprecated(forRemoval = true)
    public static @NotNull LocationPos from(@NotNull Location l){
        return new LocationPos(l.getX(), l.getY(), l.getZ());
    }
    @Deprecated(forRemoval = true)
    public static @NotNull LocationPos from(@NotNull Vector v){
        return new LocationPos(v.getX(), v.getY(), v.getZ());
    }

    protected final double x;
    protected final double y;
    protected final double z;
    public LocationPos(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
        return CruxMath.floorBlock(x);
    }

    @Override
    public int blockY() {
        return CruxMath.floorBlock(y);
    }

    @Override
    public int blockZ() {
        return CruxMath.floorBlock(z);
    }

    @Override
    public @NotNull LocationPos add(@NotNull CruxPosition pos){
        return add(pos.blockX(), pos.blockY(), pos.blockZ());
    }

    @Override
    public @NotNull LocationPos subtract(@NotNull CruxPosition pos){
        return subtract(pos.blockX(), pos.blockY(), pos.blockZ());
    }

    @Override
    public @NotNull LocationPos subtract(double x, double y, double z) {
        return subtract((int) x, (int) y, (int) z);
    }

    @Override
    public @NotNull LocationPos add(double x, double y, double z) {
        return add((int) x, (int) y, (int) z);
    }

    public @NotNull LocationPos subtract(int x, int y, int z){
        return new LocationPos(this.x - x, this.y - y, this.z - z);
    }

    public @NotNull LocationPos add(int x, int y, int z){
        return new LocationPos(this.x + x, this.y + y, this.z + z);
    }

    @Override
    public @NotNull LocationPos rotateAroundX(@NotNull CruxPosition center, double angle) {
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
        return new LocationPos(this.x, rotatedY + center.blockY(), rotatedZ + center.blockZ());
    }

    @Override
    public @NotNull LocationPos rotateAroundY(@NotNull CruxPosition center, double angle) {
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
        return new LocationPos(rotatedX + center.blockX(), this.y, rotatedZ + center.blockZ());
    }

    @Override
    public @NotNull LocationPos rotateAroundZ(@NotNull CruxPosition center, double angle) {
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
        return new LocationPos(rotatedX + center.blockX(), rotatedY + center.blockY(), this.z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LocationPos blockPos = (LocationPos) obj;
        return x == blockPos.x && y == blockPos.y && z == blockPos.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "LocationPos{x=" + x + ", y=" + y + ", z=" + z + "}";
    }
}
