package killercreepr.crux.data.world;

import killercreepr.crux.data.BlockPos;
import killercreepr.crux.data.LocationPos;
import killercreepr.crux.util.CruxMath;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface CruxPosition {
    static @NotNull CruxPosition location(double x, double y, double z){
        return precise(x, y, z);
    }
    static @NotNull CruxPosition location(@NotNull Block block){
        return precise(block);
    }
    static @NotNull CruxPosition location(@NotNull Location l){
        return precise(l);
    }
    static @NotNull CruxPosition location(@NotNull Vector v){
        return precise(v);
    }

    static @NotNull CruxPosition precise(double x, double y, double z){
        return new LocationPos(x, y, z);
    }
    static @NotNull CruxPosition precise(@NotNull Block block){
        return new LocationPos(block.getX(), block.getY(), block.getZ());
    }
    static @NotNull CruxPosition precise(@NotNull Location l){
        return new LocationPos(l.getX(), l.getY(), l.getZ());
    }
    static @NotNull CruxPosition precise(@NotNull Vector v){
        return new LocationPos(v.getX(), v.getY(), v.getZ());
    }


    static @NotNull CruxPosition block(int x, int y, int z){
        return new BlockPos(x, y, z);
    }
    static @NotNull CruxPosition block(@NotNull Block block){
        return new BlockPos(block.getX(), block.getY(), block.getZ());
    }
    static @NotNull CruxPosition block(@NotNull Location l){
        return new BlockPos(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }
    static @NotNull CruxPosition block(@NotNull Vector v){
        return new BlockPos(v.getBlockX(), v.getBlockY(), v.getBlockZ());
    }

    @Contract(pure = true)
    default @NotNull CruxPosition add(@NotNull CruxPosition pos){
        return add(pos.x(), pos.y(), pos.z());
    }
    @Contract(pure = true)
    default @NotNull CruxPosition subtract(@NotNull CruxPosition pos){
        return subtract(pos.x(), pos.y(), pos.z());
    }

    @Contract(pure = true)
    @NotNull CruxPosition subtract(double x, double y, double z);
    @Contract(pure = true)
    @NotNull CruxPosition add(double x, double y, double z);
    @Contract(pure = true)
    @NotNull CruxPosition rotateAroundX(@NotNull CruxPosition center, double angle);
    @Contract(pure = true)
    @NotNull CruxPosition rotateAroundY(@NotNull CruxPosition center, double angle);
    @Contract(pure = true)
    @NotNull CruxPosition rotateAroundZ(@NotNull CruxPosition center, double angle);

    double x();
    double y();
    double z();

    int blockX();
    int blockY();
    int blockZ();

    default int toChunkX(){
        return (int) Math.floor(x()) >> 4;
    }

    default int toChunkZ(){
        return (int) Math.floor(z()) >> 4;
    }

    default long toChunkKey(){
        return Chunk.getChunkKey(toChunkX(), toChunkZ());
    }

    @Contract(pure = true)
    default @NotNull Block getBlock(@NotNull World world){
        return world.getBlockAt(blockX(), blockY(), blockZ());
    }

    @Contract(pure = true)
    default @NotNull Vector toVector(){
        return new Vector(x(), y(), z());
    }

    @Contract(pure = true)
    default @NotNull Location toLocation(World world){
        return new Location(world, x(), y(), z());
    }

    default double distanceSquared(@NotNull CruxPosition pos){
        return CruxMath.square(x() - pos.x()) +
            CruxMath.square(y() - pos.y()) +
            CruxMath.square(z() - pos.z());
    }
}
