package killercreepr.crux.data.world;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;

public abstract class WorldChunkStorage<T> implements Iterable<ChunkBlockStorage<T>> {
    protected final @NotNull Map<Long, ChunkBlockStorage<T>> data;

    public WorldChunkStorage(@NotNull Map<Long, ChunkBlockStorage<T>> data) {
        this.data = data;
    }

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

    public T remove(long chunkKey, @NotNull CruxPosition pos){
        ChunkBlockStorage<T> container = data.get(chunkKey);
        if(container==null) return null;
        T removed = container.remove(pos);
        if(container.isEmpty()) remove(chunkKey);
        return removed;
    }

    public @Nullable ChunkBlockStorage<T> get(long chunkKey){
        return data.get(chunkKey);
    }

    public @Nullable T get(long chunkKey, @NotNull CruxPosition pos){
        if(!data.containsKey(chunkKey)) return null;
        return data.get(chunkKey).get(pos);
    }

    public T add(long chunkKey, @NotNull T block){
        /*if(data.containsKey(chunkKey)){
            return data.get(chunkKey).add(block);
        }
        data.put(chunkKey, newChunkStorage());
        return data.get(chunkKey).add(block);*/
        return data.computeIfAbsent(chunkKey, (i) -> newChunkStorage()).add(block);
    }

    public boolean isEmpty(){
        return data.isEmpty();
    }

    public abstract @NotNull ChunkBlockStorage<T> newChunkStorage();

    public boolean removeIf(Predicate<ChunkBlockStorage<T>> predicate){
        return data.values().removeIf(predicate);
    }

    @NotNull
    @Override
    public Iterator<ChunkBlockStorage<T>> iterator() {
        return data.values().iterator();
    }
}
