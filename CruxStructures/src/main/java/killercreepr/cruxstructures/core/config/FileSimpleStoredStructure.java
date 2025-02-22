package killercreepr.cruxstructures.core.config;

import killercreepr.crux.core.data.world.SimpleStoredChunk;
import killercreepr.crux.core.math.BlockPos;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxstructures.api.component.StoredStructureComponent;
import killercreepr.cruxstructures.api.component.StructureComponent;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.core.registries.StructureRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileSimpleStoredStructure<T extends StoredStructure> extends SimpleFileHandler<T> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull T object) {
        FileRegistry registry = context.getRegistry();
        FileObject o = new FileObject()
            .add("structure", registry.serializeToFile(object.getParent().key()))
            .add("chunk", registry.serializeToFile(object.getChunk()))
            .add("center", registry.serializeToFile(object.getPosition()))
            .addProperty("rotation", object.getRotation())
            ;
        FileObject data = new FileObject();
        object.getAllOfType(StoredStructureComponent.class).forEach(component ->{
            component.onFileSave(context, data, object);
        });
        if(!data.isEmpty()) o.add("data", data);
        return o;
    }

    @Override
    public @Nullable T deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        return (T) deserializeSimple(context, e);
    }

    public @Nullable StoredStructure deserializeSimple(@NotNull FileContext<?> context, @NotNull FileElement e){
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
        Key structureKey = registry.deserializeFromFile(Key.class, o.get("structure"));
        if(structureKey==null) return null;

        SimpleStoredChunk chunk = registry.deserializeFromFile(SimpleStoredChunk.class, o.get("chunk"));
        BlockPos center = registry.deserializeFromFile(BlockPos.class, o.get("center"));

        if(chunk == null || center == null) return null;

        Double rotation = registry.deserializeFromFile(Double.class, o.get("rotation"));
        if(rotation==null) rotation = 0D;

        Structure structure = StructureRegistries.STRUCTURES.get(structureKey);
        if(structure == null) throw new RuntimeException("Structure " + structureKey + " not found!");

        FileObject data;
        if(o.get("data") instanceof FileObject oop) data = oop;
        else data = new FileObject();

        StoredStructure stored = structure.buildStored(chunk, center, rotation);
        if(stored == null) return null;
        //SimpleStoredStructure stored = new SimpleStoredStructure(structure, chunk, center, rotation);
        structure.getAllOfType(StructureComponent.class).forEach(component ->{
            component.onFileLoad(context, data, stored);
        });
        return stored;
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "simple_stored_structure";
    }
}
