package killercreepr.cruxblocks.block.component;

import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.context.BlockContext;
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
}
