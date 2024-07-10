package killercreepr.crux.data.world;

import killercreepr.crux.data.BlockPos;
import org.jetbrains.annotations.NotNull;

public interface BlockPosed {
    @NotNull
    BlockPos getBlockPos();
}
