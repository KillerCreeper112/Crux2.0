package killercreepr.cruxblocks.config.block;

import killercreepr.cruxblocks.block.standard.BushBlock;
import killercreepr.cruxblocks.block.standard.BushType;
import killercreepr.cruxblocks.block.texture.TextureData;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class CfgBushBlock extends SimpleCfgBlock implements BushBlock {
    protected final @NotNull BushType bushType;
    public CfgBushBlock(@NotNull Key key, @NotNull TextureData textureData, @NotNull BushType bushType) {
        super(key, textureData);
        this.bushType = bushType;
    }

    @Override
    public @NotNull BushType getBushType() {
        return bushType;
    }
}
