package killercreepr.cruxblocks.core.block;

import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.api.block.texture.TextureData;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;

public class SimpleBlock implements CruxBlock {
    protected final @NotNull Key key;
    protected final @NotNull TextureData textureData;
    protected final @Nullable DataComponentHandler components;
    protected @Nullable CruxBlockGroup group;
    public SimpleBlock(@NotNull Key key, @NotNull TextureData textureData, @Nullable DataComponentHandler components) {
        this.key = key;
        this.textureData = textureData;
        if(components == null){
            this.components = null;
            return;
        }
        Collection<TypedDataComponent<?>> typed = new HashSet<>();
        components.forEach(typed::add);
        this.components = DataComponentHandler.mergedAccessor(typed, () ->{
            var group = getGroup();
            return group == null ? null : group.getComponents();
        });
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
