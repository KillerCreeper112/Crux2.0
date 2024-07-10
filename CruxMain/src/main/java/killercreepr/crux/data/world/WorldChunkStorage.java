package killercreepr.crux.data.world;

import killercreepr.crux.data.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class WorldChunkStorage<T> {
    protected final @NotNull Map<Long, ChunkBlockStorage<T>> data = new HashMap<>();

    public ChunkBlockStorage<T> set(long chunkKey, @Nullable ChunkBlockStorage<T> container){
        if(container==null) return data.remove(chunkKey);
        return data.put(chunkKey, container);
    }

    public @NotNull Map<Long, ChunkBlockStorage<T>> getData() {
        return data;
    }

    public ChunkBlockStorage<T> remove(long chunkKey){
        return data.remove(chunkKey);
    }

    public T remove(long chunkKey, @NotNull BlockPos pos){
        if(!data.containsKey(chunkKey)) return null;
        ChunkBlockStorage<T> container = data.get(chunkKey);
        T removed = container.remove(pos);
        if(container.isEmpty()) data.remove(chunkKey);
        return removed;
    }

    public @Nullable T get(long chunkKey, @NotNull BlockPos pos){
        if(!data.containsKey(chunkKey)) return null;
        return data.get(chunkKey).get(pos);
    }

    public T add(long chunkKey, @NotNull T block){
        return data.computeIfAbsent(chunkKey, (i) -> newChunkStorage()).add(block);
    }

    public boolean isEmpty(){
        return data.isEmpty();
    }

    public abstract @NotNull ChunkBlockStorage<T> newChunkStorage();
}
