package killercreepr.crux.world;

import killercreepr.crux.block.CruxedBlockState;
import killercreepr.crux.data.world.CruxPosition;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
@ApiStatus.Experimental
public interface CruxedWorldAccessor {
    default void setBlock(@NotNull CruxPosition position, @NotNull CruxedBlockState state){
        setBlock(position, state, true);
    }
    void setBlock(@NotNull CruxPosition position, @NotNull CruxedBlockState state, boolean applyPhysics);
}
