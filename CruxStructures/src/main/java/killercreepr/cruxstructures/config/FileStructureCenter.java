package killercreepr.cruxstructures.config;

import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.structure.generation.center.StructureCenter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileStructureCenter extends PureYamlFileHandler<StructureCenter> {
    public final MappedRegistry<String, FileObjectHandler<? extends StructureCenter>> TYPE_HANDLERS = new SimpleMappedRegistry<>();

    @Override
    public @Nullable StructureCenter deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "type");
        if(type==null) return null;
        FileObjectHandler<? extends  StructureCenter> handler = TYPE_HANDLERS.get(type);
        if(handler==null) throw new RuntimeException("No type handler found for " + type + "!");
        return handler.deserializeFromFile(context, e);
    }
}
