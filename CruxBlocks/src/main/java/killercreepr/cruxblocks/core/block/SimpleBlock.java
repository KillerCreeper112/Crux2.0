package killercreepr.cruxblocks.core.block;

import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.api.block.texture.TextureData;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        if(components != null) return components;
        if(group == null) throw new IllegalStateException(this + " does not have a group or a DataComponentHandler set!");
        return group.getComponents();
    }
}
