package killercreepr.cruxblocks.core.config.handler;

import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.texture.TextureData;
import killercreepr.cruxblocks.core.block.SimpleBlock;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileCruxBlock implements FileObjectHandler<CruxBlock> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull CruxBlock object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable CruxBlock deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        Key key = registry.deserializeFromFile(Key.class, o.get("key"));
        if(key==null) return null;
        return deserialize(ctx, e, key);
    }

    public static @Nullable CruxBlock deserialize(@NotNull FileContext<?> ctx, @NotNull FileElement e, @NotNull Key key) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        TextureData texture = registry.deserializeFromFile(TextureData.class, o.get("texture"));
        if(texture==null) return null;

        DataComponentHandler dataComponents  = registry.deserializeFromFile(DataComponentHandler.class, o.get("components"));
        return new SimpleBlock(
            key, texture, dataComponents
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "crux_block";
    }
}
