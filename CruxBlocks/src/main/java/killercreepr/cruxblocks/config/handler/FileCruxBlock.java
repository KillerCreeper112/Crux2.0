package killercreepr.cruxblocks.config.handler;

import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.component.BushType;
import killercreepr.cruxblocks.block.group.CruxDirectionalBlockGroup;
import killercreepr.cruxblocks.block.texture.TextureData;
import killercreepr.cruxblocks.config.block.CfgBushBlock;
import killercreepr.cruxblocks.config.block.CfgDirectionBlock;
import killercreepr.cruxblocks.config.block.SimpleCfgBlock;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import net.kyori.adventure.key.Key;
import org.bukkit.Axis;
import org.bukkit.block.BlockFace;
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

        BlockFace direction = null;
        try{
            direction = registry.deserializeFromFile(BlockFace.class, o.get("direction"));
        }catch (ClassCastException ignored){}

        if(direction == null){
            Axis axis = registry.deserializeFromFile(Axis.class, o.get("direction"));
            if(axis != null) direction = CruxDirectionalBlockGroup.getFaceFromAxis(axis);
        }
        if(direction != null) return new CfgDirectionBlock(key, texture, direction);

        BushType bushType = registry.deserializeFromFile(BushType.class, o.get("bush_type"));
        if(bushType != null) return new CfgBushBlock(key, texture, bushType);

        return new SimpleCfgBlock(
            key, texture
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "crux_block";
    }
}
