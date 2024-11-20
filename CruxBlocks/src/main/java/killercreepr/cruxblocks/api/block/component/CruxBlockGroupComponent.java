package killercreepr.cruxblocks.api.block.component;

import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.api.block.context.BlockContext;
import killercreepr.cruxblocks.api.block.context.PlaceBlockContext;
import killercreepr.cruxblocks.api.block.group.CruxBlockGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxBlockGroupComponent {
    default @Nullable Boolean canPlace(@NotNull BlockContext ctx, @NotNull CruxBlockGroup group) {
        return null;
    }

    default @Nullable ActiveCruxBlock placeBlock(@NotNull PlaceBlockContext ctx, boolean applyPhysics, @NotNull CruxBlockGroup group) {
        return null;
    }

    default void onRegistered(@NotNull CruxBlock block, @NotNull CruxBlockGroup group){

    }
}
