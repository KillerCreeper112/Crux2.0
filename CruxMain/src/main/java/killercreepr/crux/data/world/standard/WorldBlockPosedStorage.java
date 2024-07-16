package killercreepr.crux.data.world.standard;

import killercreepr.crux.data.world.PositionPosed;
import killercreepr.crux.data.world.ChunkBlockStorage;
import killercreepr.crux.data.world.WorldChunkStorage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class WorldBlockPosedStorage<T extends PositionPosed> extends WorldChunkStorage<T> {
    public WorldBlockPosedStorage(@NotNull Map<Long, ChunkBlockStorage<T>> data) {
        super(data);
    }

    @Override
    public @NotNull ChunkBlockStorage<T> newChunkStorage() {
        return new ChunkBlockPosedStorage<>(new HashMap<>());
    }
}
