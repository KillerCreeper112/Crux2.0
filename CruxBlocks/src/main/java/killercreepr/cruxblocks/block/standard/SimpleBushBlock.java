package killercreepr.cruxblocks.block.standard;

import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.SimpleBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.component.BushBlock;
import killercreepr.cruxblocks.block.component.BushType;
import killercreepr.cruxblocks.block.context.BlockContext;
import killercreepr.cruxblocks.block.standard.active.ActiveBushBlock;
import killercreepr.cruxblocks.block.texture.TextureData;
import killercreepr.cruxblocks.registeries.CruxBlocksRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public class SimpleBushBlock extends SimpleBlock implements BushBlock {
    protected final @NotNull BushType bushType;
    public SimpleBushBlock(@NotNull Key key, @NotNull TextureData textureData, @NotNull BushType bushType) {
        super(key, textureData);
        this.bushType = bushType;
    }

    @Override
    public @NotNull BushType getBushType() {
        return bushType;
    }

    @Override
    public @NotNull ActiveCruxBlock createActive(@NotNull Block block) {
        return new ActiveBushBlock(block, this);
    }

    @Override
    public boolean canPlace(@NotNull BlockContext ctx) {
        if(bushType == BushType.BOTTOM){
            return group.canPlace(ctx);
        }
        Block b = ctx.getBlock();
        Block ground = b.getRelative(BlockFace.DOWN);

        CruxBlock active = CruxBlocksRegistries.BLOCKS.getByBlock(ground);

        if(active == null) return false;
        return group.containsBlock(active);
    }
}