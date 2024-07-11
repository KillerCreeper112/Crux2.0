package killercreepr.crux.data.world.standard;

import killercreepr.crux.data.world.BlockPosed;
import killercreepr.crux.data.world.ChunkBlockStorage;
import killercreepr.crux.data.world.WorldChunkStorage;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WorldBlockPosedStorage<T extends BlockPosed> extends WorldChunkStorage<T> {
    public WorldBlockPosedStorage(@NotNull Map<Long, ChunkBlockStorage<T>> data) {
        super(data);
    }

    @Override
    public @NotNull ChunkBlockStorage<T> newChunkStorage() {
        return new ChunkBlockPosedStorage<>(new ConcurrentHashMap<>());
    }
}
