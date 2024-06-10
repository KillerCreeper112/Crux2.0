package killercreepr.cruxblocks.block;

import killercreepr.cruxblocks.block.texture.TextureData;
import net.kyori.adventure.key.Key;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public abstract class GenericDirectionalBlock extends GenericBlock implements CruxBlockDirectional{
    protected final @NotNull BlockFace direction;
    public GenericDirectionalBlock(@NotNull Key key, @NotNull TextureData textureData, @NotNull BlockFace direction) {
        super(key, textureData);
        this.direction = direction;
    }

    @Override
    public @NotNull BlockFace getDirection() {
        return direction;
    }
}
