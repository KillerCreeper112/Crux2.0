package killercreepr.crux.data;

import killercreepr.crux.data.world.CruxPosition;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class StoredChunk extends StoredWorld {
    public static @NotNull StoredChunk from(@NotNull Chunk chunk){
        return new StoredChunk(chunk.getWorld().getUID(), chunk.getX(), chunk.getZ());
    }

    public static @NotNull StoredChunk from(@NotNull Location location){
        return fromWorldPosition(location.getWorld().getUID(), location.getBlockX(), location.getBlockZ());
    }

    public static @NotNull StoredChunk from(@NotNull Block block){
        return fromWorldPosition(block.getWorld().getUID(), block.getX(), block.getZ());
    }

    public static @NotNull StoredChunk fromWorldPosition(@NotNull UUID worldUUID, int locationX, int locationZ){
        int chunkX = locationX >> 4;
        int chunkZ = locationZ >> 4;
        return new StoredChunk(worldUUID, chunkX, chunkZ);
    }

    public static @NotNull StoredChunk from(@NotNull StoredWorld world, @NotNull CruxPosition position){
        return fromWorldPosition(world.worldUUID(), position.blockX(), position.blockZ());
    }

    protected final int chunkX;
    protected final int chunkZ;
    public StoredChunk(@NotNull UUID uuid, int chunkX, int chunkZ) {
        super(uuid);
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    public boolean isLoaded(){
        World world = toBukkitWorld();
        return world != null && isLoaded(world);
    }

    public boolean isLoaded(@NotNull World world){
        return world.isChunkLoaded(chunkX, chunkZ);
    }

    public @NotNull Location toBukkitCenter(double y){
        return toBukkitCenter(toBukkitWorld(), y);
    }

    public @NotNull Location toBukkitCenter(@Nullable World world, double y) {
        int x = this.chunkX << 4;
        int z = this.chunkZ << 4;
        return new Location(world, x + 7, y, z + 7);
    }

    public int chunkX() {
        return chunkX;
    }

    public int chunkZ() {
        return chunkZ;
    }

    public long getChunkKey() {
        return Chunk.getChunkKey(chunkX, chunkZ);
    }

    public @Nullable Chunk toBukkitChunk(){
        World world = toBukkitWorld();
        if(world==null) return null;
        return world.getChunkAt(chunkX, chunkZ);
    }

    @Override
    public String toString() {
        return "StoredChunk{worldUUID=" + worldUUID + ", chunkX=" + chunkX + ", chunkZ=" + chunkZ + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        StoredChunk chunkPos = (StoredChunk) obj;
        return chunkX == chunkPos.chunkX && chunkZ == chunkPos.chunkZ && worldUUID.equals(chunkPos.worldUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worldUUID, chunkX, chunkZ);
    }
}
