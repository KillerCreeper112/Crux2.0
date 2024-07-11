package killercreepr.crux.data.world;

import killercreepr.crux.data.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class ChunkBlockStorage<T> {
    protected final @NotNull Map<BlockPos, T> data;

    public ChunkBlockStorage(@NotNull Map<BlockPos, T> data) {
        this.data = data;
    }

    public @NotNull Map<BlockPos, T> getData() {
        return data;
    }

    public @Nullable T get(@NotNull BlockPos pos){
        return data.get(pos);
    }

    public T add(@NotNull T block){
        return data.put(getBlockPos(block), block);
    }

    public T remove(@NotNull T block){
        return remove(getBlockPos(block));
    }

    public T remove(@NotNull BlockPos pos){
        return data.remove(pos);
    }

    public boolean isEmpty(){
        return data.isEmpty();
    }

    public abstract @NotNull BlockPos getBlockPos(@NotNull T object);
}
