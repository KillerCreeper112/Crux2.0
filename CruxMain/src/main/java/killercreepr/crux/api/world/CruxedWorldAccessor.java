package killercreepr.crux.api.world;

import killercreepr.crux.api.block.CruxedBlockState;
import killercreepr.crux.api.math.CruxPosition;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
@ApiStatus.Experimental
public interface CruxedWorldAccessor {
    default void setBlock(@NotNull CruxPosition position, @NotNull CruxedBlockState state){
        setBlock(position, state, true);
    }
    void setBlock(@NotNull CruxPosition position, @NotNull CruxedBlockState state, boolean applyPhysics);
}
