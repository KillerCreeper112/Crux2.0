package killercreepr.cruxblocks.config.handler;

import killercreepr.cruxblocks.block.texture.TextureData;
import killercreepr.cruxconfig.config.bukkit.handler.FileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FileTextureData implements FileHandler<TextureData> {
    protected final @NotNull Map<String, FileHandler<? extends TextureData>> handlers;

    public FileTextureData(@NotNull Map<String, FileHandler<? extends TextureData>> handlers) {
        this.handlers = handlers;
    }

    public @NotNull Map<String, FileHandler<? extends TextureData>> getHandlers() {
        return handlers;
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull TextureData object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable TextureData deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "type");
        if(type==null) return null;
        FileHandler<? extends TextureData> handler = handlers.get(type.toLowerCase());
        if(handler==null) return null;
        return handler.deserializeFromFile(ctx, e);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "texture_data";
    }
}
