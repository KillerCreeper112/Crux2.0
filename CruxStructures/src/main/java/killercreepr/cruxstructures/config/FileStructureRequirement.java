package killercreepr.cruxstructures.config;

import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.crux.core.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.structure.generation.requirement.StructureRequirement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileStructureRequirement extends PureYamlFileHandler<StructureRequirement> {
    public final MappedRegistry<String, FileObjectHandler<? extends StructureRequirement>> TYPE_HANDLERS = new SimpleMappedRegistry<>();
    @Override
    public @Nullable StructureRequirement deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "type");
        if(type==null) return null;
        FileObjectHandler<? extends  StructureRequirement> handler = TYPE_HANDLERS.get(type);
        if(handler==null) throw new RuntimeException("No type handler found for " + type + "!");
        return handler.deserializeFromFile(context, e);
    }
}
