package killercreepr.cruxblocks.block.standard;

import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.GenericBlock;
import killercreepr.cruxblocks.block.context.BlockContext;
import killercreepr.cruxblocks.block.texture.TextureData;
import killercreepr.cruxblocks.registeries.CruxBlocksRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public class BushBlock extends GenericBlock {
    protected final @NotNull BushType bushType;
    public BushBlock(@NotNull Key key, @NotNull TextureData textureData, @NotNull BushType bushType) {
        super(key, textureData);
        this.bushType = bushType;
    }

    public @NotNull BushType getBushType() {
        return bushType;
    }

    @Override
    public boolean canPlace(@NotNull BlockContext ctx) {
        Block b = ctx.getBlock();
        Block ground = b.getRelative(BlockFace.DOWN);
        CruxBlock active = CruxBlocksRegistries.BLOCKS.getByBlock(ground);

        if(active == null) return false;
        return group.containsBlock(active);
    }
}
