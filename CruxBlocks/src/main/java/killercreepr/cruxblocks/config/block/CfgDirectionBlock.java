package killercreepr.cruxblocks.config.block;

import killercreepr.cruxblocks.block.CruxBlockDirectional;
import killercreepr.cruxblocks.block.texture.TextureData;
import net.kyori.adventure.key.Key;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public class CfgDirectionBlock extends SimpleCfgBlock implements CruxBlockDirectional {
    protected final @NotNull BlockFace direction;
    public CfgDirectionBlock(@NotNull Key key, @NotNull TextureData textureData, @NotNull BlockFace direction) {
        super(key, textureData);
        this.direction = direction;
    }

    @Override
    public @NotNull BlockFace getDirection() {
        return direction;
    }
}
