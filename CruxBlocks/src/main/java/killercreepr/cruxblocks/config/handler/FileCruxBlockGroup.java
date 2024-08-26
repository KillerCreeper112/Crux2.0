package killercreepr.cruxblocks.config.handler;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.data.communication.CreateBlockSoundGroup;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.CruxBlockDirectional;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.config.block.CfgGenericBlockGroup;
import killercreepr.cruxblocks.config.block.CfgGenericDirectionalBlockGroup;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
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

        float hardness = o.getOrDefaultObject(Float.class, "hardness", 1f);
        CreateBlockSoundGroup soundGroup = registry.deserializeFromFile(CreateBlockSoundGroup.class, o.get("sound_group"));
        if(blocks.getFirst() instanceof CruxBlockDirectional){
            boolean orientable = o.getOrDefaultObject(Boolean.class, "orientable", false);
            return new CfgGenericDirectionalBlockGroup(
                key,orientable, hardness, soundGroup, blocksArray
            );
        }
        return new CfgGenericBlockGroup(key, hardness, soundGroup, blocksArray);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "crux_block_group";
    }
}
