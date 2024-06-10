package killercreepr.cruxblocks.block;

import killercreepr.cruxblocks.block.texture.TextureData;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public abstract class GenericBlock implements CruxBlock {
    protected final @NotNull Key key;
    protected final @NotNull TextureData textureData;
    public GenericBlock(@NotNull Key key, @NotNull TextureData textureData) {
        this.key = key;
        this.textureData = textureData;
    }

    @Override
    public @NotNull TextureData getTextureData() {
        return textureData;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
