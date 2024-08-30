package killercreepr.cruxstructures.config;

import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.structure.generation.requirement.StructureChunkRequirement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileStructureChunkRequirement extends PureYamlFileHandler<StructureChunkRequirement> {
    public final MappedRegistry<String, FileObjectHandler<? extends StructureChunkRequirement>> TYPE_HANDLERS = new SimpleMappedRegistry<>();
    @Override
    public @Nullable StructureChunkRequirement deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "type");
        if(type==null) return null;
        FileObjectHandler<? extends  StructureChunkRequirement> handler = TYPE_HANDLERS.get(type);
        if(handler==null) throw new RuntimeException("No type handler found for " + type + "!");
        return handler.deserializeFromFile(context, e);
    }
}
