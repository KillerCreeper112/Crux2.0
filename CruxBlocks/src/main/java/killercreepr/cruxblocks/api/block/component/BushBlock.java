package killercreepr.cruxblocks.api.block.component;

import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.api.block.context.BlockContext;
import killercreepr.cruxblocks.core.block.active.standard.ActiveBushBlock;
import killercreepr.cruxblocks.core.registries.CruxBlocksRegistries;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BushBlock extends CruxBlockComponent {
    @NotNull
    BushType getBushType();

    @Override
    default @Nullable ActiveCruxBlock createActive(@NotNull Block block, @NotNull CruxBlock crux) {
        return new ActiveBushBlock(block, crux, getBushType());
    }

    @Override
    @Nullable
    default Boolean canPlace(@NotNull BlockContext ctx, @NotNull CruxBlock block) {
        BushType bushType = getBushType();
        if(bushType == BushType.BOTTOM){
            return block.getGroup().canPlace(ctx);
        }
        Block b = ctx.getBlock();
        Block ground = b.getRelative(BlockFace.DOWN);

        CruxBlock active = CruxBlocksRegistries.BLOCK.getByBlock(ground);

        if(active == null) return false;
        return block.getGroup().containsBlock(active);
    }

    class Simple implements BushBlock{
        protected final @NotNull BushType bushType;

        public Simple(@NotNull BushType bushType) {
            this.bushType = bushType;
        }

        @Override
        public @NotNull BushType getBushType() {
            return bushType;
        }
    }
}
