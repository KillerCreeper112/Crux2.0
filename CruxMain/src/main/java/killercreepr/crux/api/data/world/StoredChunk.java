package killercreepr.crux.api.data.world;

import killercreepr.crux.core.data.world.SimpleStoredChunk;
import net.kyori.adventure.key.Key;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StoredChunk extends StoredWorld {
    static StoredChunk storedChunk(@NotNull Chunk chunk){
        return storedChunk(chunk.getWorld().key(), chunk.getX(), chunk.getZ());
    }
    static StoredChunk storedChunk(@NotNull Location location){
        return storedChunkFromWorldPos(location.getWorld().key(), location.getBlockX(), location.getBlockZ());
    }
    static StoredChunk storedChunk(@NotNull Block block){
        return storedChunkFromWorldPos(block.getWorld().key(), block.getX(), block.getZ());
    }
    static StoredChunk storedChunkFromWorldPos(@NotNull Key worldKey, int locationX, int locationZ){
        int chunkX = locationX >> 4;
        int chunkZ = locationZ >> 4;
        return storedChunk(worldKey, chunkX, chunkZ);
    }

    static StoredChunk storedChunk(@NotNull Key worldKey, int chunkX, int chunkZ){
        return new SimpleStoredChunk(worldKey, chunkX, chunkZ);
    }

    int chunkX();
    int chunkZ();
    long getChunkKey();
    @Nullable Chunk toBukkitChunk();
    @NotNull Location toBukkitCenter(@Nullable World world, double y);
    @NotNull Location toBukkitCenter(double y);
    boolean isLoaded(@NotNull World world);
    boolean isLoaded();
}
