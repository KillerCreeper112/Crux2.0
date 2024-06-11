package killercreepr.cruxblocks.block;

import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlockImpl;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.block.texture.TextureData;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GenericBlock implements CruxBlock {
    protected final @NotNull Key key;
    protected final @NotNull TextureData textureData;
    protected @Nullable CruxBlockGroup group;
    public GenericBlock(@NotNull Key key, @NotNull TextureData textureData) {
        this.key = key;
        this.textureData = textureData;
    }

    @Override
    public @NotNull ActiveCruxBlock createActive(@NotNull Block block) {
        return new ActiveCruxBlockImpl(block, this);
    }

    @Override
    public @Nullable CruxBlockGroup getGroup() {
        return group;
    }

    protected GenericBlock setGroup(@Nullable CruxBlockGroup group) {
        this.group = group;
        return this;
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
