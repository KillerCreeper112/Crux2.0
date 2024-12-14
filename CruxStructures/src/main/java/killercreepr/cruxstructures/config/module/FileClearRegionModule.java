package killercreepr.cruxstructures.config.module;

import killercreepr.crux.api.valueproviders.vector.NumberVector;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.structure.module.standard.ClearRegionModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileClearRegionModule extends PureYamlFileHandler<ClearRegionModule> {
    @Override
    public @Nullable ClearRegionModule deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
        return new ClearRegionModule(
            registry.deserializeFromFile(NumberVector.class, o.get("pos1")),
            registry.deserializeFromFile(NumberVector.class, o.get("pos2"))
        );
    }
}
