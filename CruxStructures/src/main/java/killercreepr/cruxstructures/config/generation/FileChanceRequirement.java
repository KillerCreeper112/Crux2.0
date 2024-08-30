package killercreepr.cruxstructures.config.generation;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.structure.generation.requirement.StructureChanceRequirement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileChanceRequirement extends PureYamlFileHandler<StructureChanceRequirement> {
    @Override
    public @Nullable StructureChanceRequirement deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        Float chance = o.getObject(Float.class, "chance");
        if(chance == null || chance <= 0f) return null;
        return new StructureChanceRequirement(chance);
    }

}
