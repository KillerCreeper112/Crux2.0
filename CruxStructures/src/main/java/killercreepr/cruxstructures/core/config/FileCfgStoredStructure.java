package killercreepr.cruxstructures.core.config;

import killercreepr.crux.core.data.world.StoredChunk;
import killercreepr.crux.core.math.BlockPos;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import killercreepr.cruxstructures.core.structure.stored.CfgStoredStructure;
import net.kyori.adventure.key.Key;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileCfgStoredStructure<T extends StoredStructure> extends FileSimpleStoredStructure<T>{
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull T object) {
        FileRegistry reg = context.getRegistry();
        FileObject o = (FileObject) super.serializeToFile(context, object);
        if(object instanceof CfgStoredStructure stored){
            o.add("bounding_box", reg.serializeToFile(stored.getBoundingBox()));
            BoundingBox inner = stored.getInnerBox();
            if(inner != null){
                o.add("inner_bounding_box", reg.serializeToFile(inner));
            }
        }
        return o;
    }

    @Override
    public @Nullable CfgStoredStructure deserializeSimple(@NotNull FileContext<?> context, @NotNull FileElement e){
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
        Key structureKey = registry.deserializeFromFile(Key.class, o.get("structure"));
        if(structureKey==null) return null;

        StoredChunk chunk = registry.deserializeFromFile(StoredChunk.class, o.get("chunk"));
        BlockPos center = registry.deserializeFromFile(BlockPos.class, o.get("center"));

        if(chunk == null || center == null) return null;

        Double rotation = registry.deserializeFromFile(Double.class, o.get("rotation"));
        if(rotation==null) rotation = 0D;

        BoundingBox box = registry.deserializeFromFile(BoundingBox.class, o.get("bounding_box"));
        if(box == null) return null;
        BoundingBox innerBox = registry.deserializeFromFile(BoundingBox.class, o.get("inner_bounding_box"));

        return new CfgStoredStructure(structureKey, chunk, center, box, rotation, innerBox);
    }
}
