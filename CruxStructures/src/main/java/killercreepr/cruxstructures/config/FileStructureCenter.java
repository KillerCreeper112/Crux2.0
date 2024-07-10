package killercreepr.cruxstructures.config;

import com.google.gson.JsonObject;
import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.bukkit.handler.FileHandler;
import killercreepr.cruxconfig.config.bukkit.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.structure.StructureCenter;
import killercreepr.cruxstructures.structure.impl.SurfaceCenter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileStructureCenter extends SimpleFileHandler<StructureCenter> {
    public final MappedRegistry<String, FileHandler<? extends StructureCenter>> TYPE_HANDLERS = new SimpleMappedRegistry<>();
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull StructureCenter object) {
        FileHandler handler = TYPE_HANDLERS.get(object.type());
        if(handler==null) throw new RuntimeException("No type handler found for " + object.type() + "!");
        return handler.serializeToFile(context, object);
    }

    @Override
    public @Nullable StructureCenter deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "type");
        if(type==null) return null;
        FileHandler handler = TYPE_HANDLERS.get(type);
        if(handler==null) throw new RuntimeException("No type handler found for " + type + "!");
        return null;//todo
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "structure_center";
    }
}
