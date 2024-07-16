package killercreepr.crux.data.world;

import killercreepr.crux.data.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;

public abstract class ChunkBlockStorage<T> implements Iterable<T> {
    protected final @NotNull Map<CruxPosition, T> data;

    public ChunkBlockStorage(@NotNull Map<CruxPosition, T> data) {
        this.data = data;
    }

    public @NotNull Map<CruxPosition, T> getData() {
        return data;
    }

    public @Nullable T get(@NotNull CruxPosition pos){
        return data.get(pos);
    }

    public T add(@NotNull T block){
        return data.put(getBlockPos(block), block);
    }

    public T remove(@NotNull T block){
        return remove(getBlockPos(block));
    }

    public T remove(@NotNull CruxPosition pos){
        return data.remove(pos);
    }

    public boolean isEmpty(){
        return data.isEmpty();
    }

    public abstract @NotNull CruxPosition getBlockPos(@NotNull T object);

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return data.values().iterator();
    }
}
