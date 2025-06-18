package killercreepr.cruxblocks.api.block.component;

import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.api.block.context.BlockContext;
import killercreepr.cruxblocks.core.block.active.standard.ActiveBushBlock;
import killercreepr.cruxblocks.core.block.active.standard.ActiveVineBlock;
import killercreepr.cruxblocks.core.registries.CruxBlocksRegistries;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VineBlock extends CruxBlockComponent {
    @NotNull
    VineType getVineType();

    @Override
    default @Nullable ActiveCruxBlock createActive(@NotNull Block block, @NotNull CruxBlock crux) {
        return new ActiveVineBlock(block, crux, getVineType());
    }

    /*@Override
    @Nullable
    default Boolean canPlace(@NotNull BlockContext ctx, @NotNull CruxBlock block) {
        VineType bushType = getVineType();
        if(bushType == VineType.BOTTOM){
            return block.getGroup().canPlace(ctx);
        }
        Block b = ctx.getBlock();
        Block ground = b.getRelative(BlockFace.DOWN);

        CruxBlock active = CruxBlocksRegistries.BLOCK.getByBlock(ground);

        if(active == null) return false;
        return block.getGroup().containsBlock(active);
    }*/

    class Simple implements VineBlock {
        protected final @NotNull VineType vineType;

        public Simple(@NotNull VineType vineType) {
            this.vineType = vineType;
        }

        @Override
        public @NotNull VineType getVineType() {
            return vineType;
        }
    }
}
