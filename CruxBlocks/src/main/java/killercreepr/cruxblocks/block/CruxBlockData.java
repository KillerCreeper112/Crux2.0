package killercreepr.cruxblocks.block;

import killercreepr.crux.component.DataComponentHandler;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.context.BlockContext;
import killercreepr.cruxblocks.block.context.PlaceBlockContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxBlockData {
    @NotNull
    DataComponentHandler getComponents();
    boolean canPlace(@NotNull BlockContext ctx);
    default @Nullable ActiveCruxBlock placeBlock(@NotNull PlaceBlockContext context){
        return placeBlock(context, true);
    }
    @Nullable ActiveCruxBlock placeBlock(@NotNull PlaceBlockContext ctx, boolean applyPhysics);
}
