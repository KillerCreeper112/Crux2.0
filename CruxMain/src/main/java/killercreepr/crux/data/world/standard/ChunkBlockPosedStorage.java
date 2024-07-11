package killercreepr.crux.data.world.standard;

import killercreepr.crux.data.BlockPos;
import killercreepr.crux.data.world.BlockPosed;
import killercreepr.crux.data.world.ChunkBlockStorage;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ChunkBlockPosedStorage<T extends BlockPosed> extends ChunkBlockStorage<T> {
    public ChunkBlockPosedStorage(@NotNull Map<BlockPos, T> data) {
        super(data);
    }

    @Override
    public @NotNull BlockPos getBlockPos(@NotNull T object) {
        return object.getBlockPos();
    }
}
