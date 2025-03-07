package killercreepr.cruxblocks.api.block.component;

import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.api.block.context.BlockContext;
import killercreepr.cruxblocks.api.event.CruxBlockBreakEvent;
import killercreepr.cruxblocks.api.event.CruxBlockPlaceEvent;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxBlockComponent {
    default @Nullable Boolean canPlace(@NotNull BlockContext ctx, @NotNull CruxBlock block){
        return null;
    }
    default @Nullable ActiveCruxBlock createActive(@NotNull Block block, @NotNull CruxBlock crux){
        return null;
    }
    default void onBroken(@NotNull CruxBlockBreakEvent event){}
    default void onPlaced(@NotNull CruxBlockPlaceEvent event){}
}
