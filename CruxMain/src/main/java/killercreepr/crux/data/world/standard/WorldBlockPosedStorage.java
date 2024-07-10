package killercreepr.crux.data.world.standard;

import killercreepr.crux.data.world.BlockPosed;
import killercreepr.crux.data.world.ChunkBlockStorage;
import killercreepr.crux.data.world.WorldChunkStorage;
import org.jetbrains.annotations.NotNull;

public class WorldBlockPosedStorage<T extends BlockPosed> extends WorldChunkStorage<T> {
    @Override
    public @NotNull ChunkBlockStorage<T> newChunkStorage() {
        return new ChunkBlockPosedStorage<>();
    }
}
