package killercreepr.crux.data.world.standard;

import killercreepr.crux.data.world.BlockPosed;
import killercreepr.crux.data.world.MultiVerseWorldStorage;
import killercreepr.crux.data.world.WorldChunkStorage;
import org.jetbrains.annotations.NotNull;

public class MultiVerseBlockPosedStorage<T extends BlockPosed> extends MultiVerseWorldStorage<T> {
    @Override
    public @NotNull WorldChunkStorage<T> newWorldStorage() {
        return new WorldBlockPosedStorage<>();
    }
}
