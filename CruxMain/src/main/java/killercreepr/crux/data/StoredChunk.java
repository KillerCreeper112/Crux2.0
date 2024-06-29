package killercreepr.crux.data;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class StoredChunk extends StoredWorld {
    public static @NotNull StoredChunk from(@NotNull Chunk chunk){
        return new StoredChunk(chunk.getWorld().getUID(), chunk.getX(), chunk.getZ());
    }

    protected final int x;
    protected final int z;
    public StoredChunk(@NotNull UUID uuid, int x, int z) {
        super(uuid);
        this.x = x;
        this.z = z;
    }

    public long getChunkKey() {
        return Chunk.getChunkKey(x, z);
    }

    public @Nullable Chunk toBukkitChunk(){
        World world = toBukkitWorld();
        if(world==null) return null;
        return world.getChunkAt(x, z);
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
        return x == chunkPos.x && z == chunkPos.z && worldUUID.equals(chunkPos.worldUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worldUUID, x, z);
    }
}
