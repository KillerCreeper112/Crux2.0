package killercreepr.crux.core.data.world;

import killercreepr.crux.api.data.world.MultiVerseWorldStorage;
import killercreepr.crux.api.math.PositionPosed;
import killercreepr.crux.api.data.world.WorldChunkStorage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MultiVerseBlockPosedStorage<T extends PositionPosed> extends MultiVerseWorldStorage<T> {
    public MultiVerseBlockPosedStorage(@NotNull Map<UUID, WorldChunkStorage<T>> data) {
        super(data);
    }

    @Override
    public @NotNull WorldChunkStorage<T> newWorldStorage() {
        return new WorldBlockPosedStorage<>(new HashMap<>());
    }
}
