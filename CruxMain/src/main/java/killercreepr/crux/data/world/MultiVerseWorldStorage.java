package killercreepr.crux.data.world;

import killercreepr.crux.data.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public abstract class MultiVerseWorldStorage<T> {
    protected final @NotNull Map<UUID, WorldChunkStorage<T>> data;

    public MultiVerseWorldStorage(@NotNull Map<UUID, WorldChunkStorage<T>> data) {
        this.data = data;
    }

    public MultiVerseWorldStorage<T> set(@NotNull UUID worldUUID, @Nullable WorldChunkStorage<T> container){
        if(container==null) data.remove(worldUUID);
        else data.put(worldUUID, container);
        return this;
    }

    public @NotNull Map<UUID, WorldChunkStorage<T>> getData() {
        return data;
    }

    public WorldChunkStorage<T> remove(@NotNull UUID worldUUID){
        return data.remove(worldUUID);
    }

    public ChunkBlockStorage<T> remove(@NotNull UUID worldUUID, long chunkKey){
        WorldChunkStorage<T> found = data.get(worldUUID);
        if(found==null) return null;
        ChunkBlockStorage<T> removed = found.remove(chunkKey);
        if(found.isEmpty()) data.remove(worldUUID);
        return removed;
    }

    public T remove(@NotNull UUID worldUUID, long chunkKey, @NotNull BlockPos pos){
        WorldChunkStorage<T> container = data.get(worldUUID);
        if(container==null) return null;
        T removed = container.remove(chunkKey, pos);
        if(container.isEmpty()) remove(worldUUID);
        return removed;
    }

    public @Nullable T get(@NotNull UUID worldUUID, long chunkKey, @NotNull BlockPos pos){
        if(!data.containsKey(worldUUID)) return null;
        return data.get(worldUUID).get(chunkKey, pos);
    }
    public @Nullable ChunkBlockStorage<T> get(@NotNull UUID worldUUID, long chunkKey){
        WorldChunkStorage<T> got = get(worldUUID);
        if(got==null) return null;
        return got.get(chunkKey);
    }

    public @Nullable WorldChunkStorage<T> get(@NotNull UUID worldUUID){
        return data.get(worldUUID);
    }

    public MultiVerseWorldStorage<T> add(@NotNull UUID worldUUID, long chunkKey, @NotNull T block){
        /*if(data.containsKey(worldUUID)){
            data.get(worldUUID).add(chunkKey, block);
        }
        data.put(worldUUID, newWorldStorage());
        data.get(worldUUID).add(chunkKey, block);
        return this;*/
        data.computeIfAbsent(worldUUID, (u) -> newWorldStorage()).add(chunkKey, block);
        return this;
    }

    public boolean isEmpty(){
        return data.isEmpty();
    }

    public abstract @NotNull WorldChunkStorage<T> newWorldStorage();
}
