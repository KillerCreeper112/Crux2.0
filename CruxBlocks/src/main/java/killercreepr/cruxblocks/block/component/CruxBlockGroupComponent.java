package killercreepr.cruxblocks.block.component;

import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.context.BlockContext;
import killercreepr.cruxblocks.block.context.PlaceBlockContext;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxBlockGroupComponent {
    default @Nullable Boolean canPlace(@NotNull BlockContext ctx, @NotNull CruxBlockGroup group) {
        return null;
    }

    default @Nullable ActiveCruxBlock placeBlock(@NotNull PlaceBlockContext ctx, boolean applyPhysics, @NotNull CruxBlockGroup group) {
        return null;
    }
}
