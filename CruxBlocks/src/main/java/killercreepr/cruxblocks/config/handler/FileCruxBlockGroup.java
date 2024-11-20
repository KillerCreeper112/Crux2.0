package killercreepr.cruxblocks.config.handler;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.block.group.SimpleBlockGroup;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FileCruxBlockGroup implements FileObjectHandler<CruxBlockGroup> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull CruxBlockGroup object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable CruxBlockGroup deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        Key key = registry.deserializeFromFile(Key.class, o.get("key"));
        if(key == null) return null;
        return deserialize(ctx, e, key);
    }

    public static @Nullable CruxBlockGroup deserialize(@NotNull FileContext<?> ctx, @NotNull FileElement e, @NotNull Key key) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        List<CruxBlock> blocks = registry.deserializeFromFile(new TypeToken<List<CruxBlock>>(){}.getType(), o.get("blocks"));
        if(blocks == null || blocks.isEmpty()) return null;

        CruxBlock[] blocksArray = blocks.toArray(new CruxBlock[0]);

        DataComponentHandler dataComponents  = registry.deserializeFromFile(DataComponentHandler.class, o.get("components"));
        if(dataComponents == null) dataComponents = DataComponentHandler.simple();
        return new SimpleBlockGroup(
            key, dataComponents,
            blocksArray
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "crux_block_group";
    }
}
