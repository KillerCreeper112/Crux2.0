package killercreepr.cruxblocks.config.block;

import killercreepr.cruxblocks.block.GenericBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.texture.TextureData;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class CfgBlock extends GenericBlock {
    public CfgBlock(@NotNull Key key, @NotNull TextureData textureData) {
        super(key, textureData);
    }

    @Override
    public @NotNull ActiveCruxBlock createActive(@NotNull Block block) {
        return new CfgActiveBlock(block, this);
    }
}
