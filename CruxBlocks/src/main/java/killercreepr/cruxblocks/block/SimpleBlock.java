package killercreepr.cruxblocks.block;

import killercreepr.crux.component.DataComponentHandler;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.block.texture.TextureData;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SimpleBlock implements CruxBlock {
    protected final @NotNull Key key;
    protected final @NotNull TextureData textureData;
    protected final @Nullable DataComponentHandler components;
    protected @Nullable CruxBlockGroup group;
    public SimpleBlock(@NotNull Key key, @NotNull TextureData textureData, @Nullable DataComponentHandler components) {
        this.key = key;
        this.textureData = textureData;
        this.components = components;
    }

    @Override
    public @Nullable CruxBlockGroup getGroup() {
        return group;
    }

    @Override
    public void setGroup(@Nullable CruxBlockGroup group) {
        this.group = group;
    }

    @Override
    public @NotNull TextureData getTextureData() {
        return textureData;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public @NotNull DataComponentHandler getComponents() {
        return group == null ? Objects.requireNonNull(components, this + " does not have a group or a DataComponentHandler set!") : group.getComponents();
    }
}
