package killercreepr.crux.data.world.standard;

import killercreepr.crux.data.world.MultiVerseWorldStorage;
import killercreepr.crux.data.world.PositionPosed;
import killercreepr.crux.data.world.WorldChunkStorage;
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
