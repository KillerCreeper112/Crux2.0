package killercreepr.crux.data.world.standard;

import killercreepr.crux.data.BlockPos;
import killercreepr.crux.data.world.BlockPosed;
import killercreepr.crux.data.world.ChunkBlockStorage;
import org.jetbrains.annotations.NotNull;

public class ChunkBlockPosedStorage<T extends BlockPosed> extends ChunkBlockStorage<T> {
    @Override
    public @NotNull BlockPos getBlockPos(@NotNull T object) {
        return object.getBlockPos();
    }
}
