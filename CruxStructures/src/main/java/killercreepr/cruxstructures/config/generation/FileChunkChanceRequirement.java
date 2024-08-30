package killercreepr.cruxstructures.config.generation;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.structure.generation.requirement.StructureChunkChanceRequirement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileChunkChanceRequirement extends PureYamlFileHandler<StructureChunkChanceRequirement> {
    @Override
    public @Nullable StructureChunkChanceRequirement deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        Float chance = o.getObject(Float.class, "chance");
        if(chance == null || chance <= 0f) return null;
        return new StructureChunkChanceRequirement(chance);
    }

}
