package killercreepr.cruxstructures.config.generation;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.structure.generation.requirement.StructureChunkPerThousandRequirement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileChunkPerThousandRequirement extends PureYamlFileHandler<StructureChunkPerThousandRequirement> {
    @Override
    public @Nullable StructureChunkPerThousandRequirement deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        int perThousand = o.getObject(Integer.class, "per_thousand", 2);
        return new StructureChunkPerThousandRequirement(perThousand);
    }

}
