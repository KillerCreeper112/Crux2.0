package killercreepr.crux.core.data.world;

import killercreepr.crux.api.data.world.StoredChunk;
import killercreepr.crux.core.Crux;
import net.kyori.adventure.key.Key;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SimpleStoredChunk  implements StoredChunk {
    protected final Key key;
    protected final int chunkX;
    protected final int chunkZ;
    public SimpleStoredChunk(@NotNull Key worldKey, int chunkX, int chunkZ) {
        this.key = worldKey;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    public boolean isLoaded(){
        World world = toBukkitWorld();
        return world != null && isLoaded(world);
    }

    @Override
    public @NotNull Key worldKey() {
        return key;
    }

    @Override
    public @Nullable World toBukkitWorld() {
        return Crux.getServer().getWorld(key);
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
        return "StoredChunk{worldKey=" + key + ", chunkX=" + chunkX + ", chunkZ=" + chunkZ + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SimpleStoredChunk chunkPos = (SimpleStoredChunk) obj;
        return chunkX == chunkPos.chunkX && chunkZ == chunkPos.chunkZ && key.equals(chunkPos.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, chunkX, chunkZ);
    }
}
