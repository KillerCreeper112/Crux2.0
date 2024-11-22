package killercreepr.crux.core.data.world;

import killercreepr.crux.api.data.world.ChunkBlockStorage;
import killercreepr.crux.api.data.world.WorldChunkStorage;
import killercreepr.crux.api.math.PositionPosed;
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
