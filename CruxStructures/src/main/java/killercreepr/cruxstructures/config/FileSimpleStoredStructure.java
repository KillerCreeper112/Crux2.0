package killercreepr.cruxstructures.config;

import killercreepr.crux.data.BlockPos;
import killercreepr.crux.data.StoredChunk;
import killercreepr.cruxconfig.config.bukkit.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.registries.StructureRegistries;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.stored.SimpleStoredStructure;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileSimpleStoredStructure extends SimpleFileHandler<SimpleStoredStructure> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull SimpleStoredStructure object) {
        FileRegistry registry = context.getRegistry();
        return new FileObject()
            .addProperty("structure", object.getParent().key().asString())
            .add("chunk", registry.serializeToFileElement(object.getChunk()))
            .add("center", registry.serializeToFileElement(object.getBlockPos()))
            ;
    }

    @Override
    public @Nullable SimpleStoredStructure deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
        Key structureKey = registry.deserialize(Key.class, o.get("structure"));
        if(structureKey==null) return null;

        StoredChunk chunk = registry.deserialize(StoredChunk.class, o.get("chunk"));
        BlockPos center = registry.deserialize(BlockPos.class, o.get("center"));

        if(chunk == null || center == null) return null;

        Structure structure = StructureRegistries.STRUCTURES.get(structureKey);
        if(structure == null) throw new RuntimeException("Structure " + structureKey + " not found!");

        return new SimpleStoredStructure(structure, chunk, center);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "simple_stored_structure";
    }
}
