package killercreepr.cruxstructures.config;

import killercreepr.crux.core.data.world.StoredChunk;
import killercreepr.crux.core.math.BlockPos;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxstructures.registries.StructureRegistries;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.stored.SimpleStoredStructure;
import killercreepr.cruxstructures.structure.stored.StoredStructure;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileSimpleStoredStructure<T extends StoredStructure> extends SimpleFileHandler<T> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull T object) {
        FileRegistry registry = context.getRegistry();
        return new FileObject()
            .add("structure", registry.serializeToFile(object.getParent().key()))
            .add("chunk", registry.serializeToFile(object.getChunk()))
            .add("center", registry.serializeToFile(object.getPosition()))
            .addProperty("rotation", object.getRotation())
            ;
    }

    @Override
    public @Nullable T deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        return (T) deserializeSimple(context, e);
    }

    public @Nullable SimpleStoredStructure deserializeSimple(@NotNull FileContext<?> context, @NotNull FileElement e){
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
        Key structureKey = registry.deserializeFromFile(Key.class, o.get("structure"));
        if(structureKey==null) return null;

        StoredChunk chunk = registry.deserializeFromFile(StoredChunk.class, o.get("chunk"));
        BlockPos center = registry.deserializeFromFile(BlockPos.class, o.get("center"));

        if(chunk == null || center == null) return null;

        Double rotation = registry.deserializeFromFile(Double.class, o.get("rotation"));
        if(rotation==null) rotation = 0D;

        Structure structure = StructureRegistries.STRUCTURES.get(structureKey);
        if(structure == null) throw new RuntimeException("Structure " + structureKey + " not found!");

        return new SimpleStoredStructure(structure, chunk, center, rotation);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "simple_stored_structure";
    }
}
