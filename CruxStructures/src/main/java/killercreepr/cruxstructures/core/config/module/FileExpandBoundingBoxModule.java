package killercreepr.cruxstructures.core.config.module;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.core.structure.component.StructureOuterBoxComponent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileExpandBoundingBoxModule extends PureYamlFileHandler<StructureOuterBoxComponent> {
    @Override
    public @Nullable StructureOuterBoxComponent deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
        return new StructureOuterBoxComponent(
            registry.deserializeFromFile(Vector.class, o.get("expansion"))
        );
    }
}
