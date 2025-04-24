package killercreepr.crux.core.math;

import killercreepr.crux.api.math.CruxPosition;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BlockPos implements CruxPosition {
    public static @NotNull BlockPos at(int x, int y, int z){
        return new BlockPos(x, y, z);
    }
    public static BlockPos at(Block b){
        return at(b.getX(), b.getY(), b.getZ());
    }
    public static @NotNull BlockPos asBlock(@NotNull CruxPosition v){
        if(v instanceof BlockPos d) return d;
        return new BlockPos(v.blockX(), v.blockY(), v.blockZ());
    }

    @Deprecated(forRemoval = true)
    public static @NotNull BlockPos from(@NotNull Block block){
        return new BlockPos(block.getX(), block.getY(), block.getZ());
    }
    @Deprecated(forRemoval = true)
    public static @NotNull BlockPos from(@NotNull Location l){
        return new BlockPos(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }
    @Deprecated(forRemoval = true)
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
        return x;
    }

    @Override
    public int blockY() {
        return y;
    }

    @Override
    public int blockZ() {
        return z;
    }

    @Override
    public @NotNull BlockPos add(@NotNull CruxPosition pos){
        return add(pos.blockX(), pos.blockY(), pos.blockZ());
    }

    @Override
    public @NotNull BlockPos subtract(@NotNull CruxPosition pos){
        return subtract(pos.blockX(), pos.blockY(), pos.blockZ());
    }

    @Override
    public @NotNull BlockPos subtract(double x, double y, double z) {
        return subtract((int) x, (int) y, (int) z);
    }

    @Override
    public @NotNull BlockPos add(double x, double y, double z) {
        return add((int) x, (int) y, (int) z);
    }

    public @NotNull BlockPos subtract(int x, int y, int z){
        return new BlockPos(this.x - x, this.y - y, this.z - z);
    }

    public @NotNull BlockPos add(int x, int y, int z){
        return new BlockPos(this.x + x, this.y + y, this.z + z);
    }

    @Override
    public @NotNull BlockPos rotateAroundX(@NotNull CruxPosition center, double angle) {
        double radians = Math.toRadians(angle);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);

        // Translate point to origin
        int translatedY = this.y - center.blockY();
        int translatedZ = this.z - center.blockZ();

        // Apply rotation matrix
        int rotatedY = (int) Math.round(translatedY * cosTheta - translatedZ * sinTheta);
        int rotatedZ = (int) Math.round(translatedY * sinTheta + translatedZ * cosTheta);

        // Translate point back
        return new BlockPos(this.x, rotatedY + center.blockY(), rotatedZ + center.blockZ());
    }

    @Override
    public @NotNull BlockPos rotateAroundY(@NotNull CruxPosition center, double angle) {
        if(angle == 0D) return this;
        double radians = Math.toRadians(angle*-1);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);

        // Translate point to origin
        int translatedX = this.x - center.blockX();
        int translatedZ = this.z - center.blockZ();

        // Apply rotation matrix
        int rotatedX = (int) Math.round(translatedX * cosTheta - translatedZ * sinTheta);
        int rotatedZ = (int) Math.round(translatedX * sinTheta + translatedZ * cosTheta);

        // Translate point back
        return new BlockPos(rotatedX + center.blockX(), this.y, rotatedZ + center.blockZ());
    }

    @Override
    public @NotNull BlockPos rotateAroundZ(@NotNull CruxPosition center, double angle) {
        double radians = Math.toRadians(angle);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);

        // Translate point to origin
        int translatedX = this.x - center.blockX();
        int translatedY = this.y - center.blockY();

        // Apply rotation matrix
        int rotatedX = (int) Math.round(translatedX * cosTheta - translatedY * sinTheta);
        int rotatedY = (int) Math.round(translatedX * sinTheta + translatedY * cosTheta);

        // Translate point back
        return new BlockPos(rotatedX + center.blockX(), rotatedY + center.blockY(), this.z);
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

    @Override
    public String toString() {
        return "BlockPos{x=" + x + ", y=" + y + ", z=" + z + "}";
    }
}
