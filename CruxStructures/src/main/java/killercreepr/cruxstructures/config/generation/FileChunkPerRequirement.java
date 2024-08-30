package killercreepr.cruxstructures.config.generation;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.structure.generation.requirement.StructureChunkPerRequirement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileChunkPerRequirement extends PureYamlFileHandler<StructureChunkPerRequirement> {
    @Override
    public @Nullable StructureChunkPerRequirement deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        int per = o.getObject(Integer.class, "per", 2);
        int chunk = o.getObject(Integer.class, "chunk", 1000);
        return new StructureChunkPerRequirement(per, chunk);
    }

}
