package killercreepr.cruxblocks.config.handler;

import com.google.common.reflect.TypeToken;
import killercreepr.cruxblocks.block.texture.WireTextureData;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public class FileWireTextureData implements FileObjectHandler<WireTextureData> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull WireTextureData object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable WireTextureData deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        boolean attached = o.getOrDefaultObject(Boolean.class, "attached", false);
        boolean disarmed = o.getOrDefaultObject(Boolean.class, "disarmed", false);
        boolean powered = o.getOrDefaultObject(Boolean.class, "powered", false);
        Collection<BlockFace> faces = registry.deserializeFromFile(
            new TypeToken<Collection<BlockFace>>(){}.getType(), o.get("faces")
        );
        return new WireTextureData(
            attached, disarmed, powered, faces == null ? Set.of() : faces
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "wire_texture_data";
    }
}
