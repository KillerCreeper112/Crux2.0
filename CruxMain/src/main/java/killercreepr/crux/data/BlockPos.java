package killercreepr.crux.data;

import org.bukkit.Location;
import org.bukkit.World;
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

    public @NotNull BlockPos add(@NotNull BlockPos pos){
        return add(pos.x(), pos.y(), pos.z());
    }

    public @NotNull BlockPos subtract(@NotNull BlockPos pos){
        return subtract(pos.x(), pos.y(), pos.z());
    }

    public @NotNull BlockPos subtract(int x, int y, int z){
        return new BlockPos(this.x - x, this.y - y, this.z - z);
    }

    public @NotNull BlockPos add(int x, int y, int z){
        return new BlockPos(this.x + x, this.y + y, this.z + z);
    }

    public BlockPos rotateAroundX(BlockPos center, double angle) {
        double radians = Math.toRadians(angle);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);

        // Translate point to origin
        int translatedY = this.y - center.y;
        int translatedZ = this.z - center.z;

        // Apply rotation matrix
        int rotatedY = (int) Math.round(translatedY * cosTheta - translatedZ * sinTheta);
        int rotatedZ = (int) Math.round(translatedY * sinTheta + translatedZ * cosTheta);

        // Translate point back
        return new BlockPos(this.x, rotatedY + center.y, rotatedZ + center.z);
    }

    public @NotNull BlockPos rotateAroundY(@NotNull BlockPos center, double angle) {
        double radians = Math.toRadians(angle*-1);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);

        // Translate point to origin
        int translatedX = this.x - center.x;
        int translatedZ = this.z - center.z;

        // Apply rotation matrix
        int rotatedX = (int) Math.round(translatedX * cosTheta - translatedZ * sinTheta);
        int rotatedZ = (int) Math.round(translatedX * sinTheta + translatedZ * cosTheta);

        // Translate point back
        return new BlockPos(rotatedX + center.x, this.y, rotatedZ + center.z);
    }

    public BlockPos rotateAroundZ(BlockPos center, double angle) {
        double radians = Math.toRadians(angle);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);

        // Translate point to origin
        int translatedX = this.x - center.x;
        int translatedY = this.y - center.y;

        // Apply rotation matrix
        int rotatedX = (int) Math.round(translatedX * cosTheta - translatedY * sinTheta);
        int rotatedY = (int) Math.round(translatedX * sinTheta + translatedY * cosTheta);

        // Translate point back
        return new BlockPos(rotatedX + center.x, rotatedY + center.y, this.z);
    }

    public @NotNull Block getBlock(@NotNull World world){
        return world.getBlockAt(x, y, z);
    }

    public @NotNull Vector toVector(){
        return new Vector(x, y, z);
    }

    public @NotNull Location toLocation(World world){
        return new Location(world, x, y, z);
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
