package killercreepr.crux.core.data.world;

import killercreepr.crux.api.data.world.ChunkBlockStorage;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.api.math.PositionPosed;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ChunkBlockPosedStorage<T extends PositionPosed> extends ChunkBlockStorage<T> {
    public ChunkBlockPosedStorage(@NotNull Map<CruxPosition, T> data) {
        super(data);
    }

    @Override
    public @NotNull CruxPosition getBlockPos(@NotNull T object) {
        return object.getPosition();
    }
}
