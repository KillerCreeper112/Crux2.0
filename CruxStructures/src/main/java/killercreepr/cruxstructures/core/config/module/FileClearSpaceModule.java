package killercreepr.cruxstructures.core.config.module;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.core.structure.module.ClearSpaceModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileClearSpaceModule extends PureYamlFileHandler<ClearSpaceModule> {
    @Override
    public @Nullable ClearSpaceModule deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
        return new ClearSpaceModule(
            registry.deserializeFromFile(NumberProvider.class, o.get("radius")),
            registry.deserializeFromFile(NumberProvider.class, o.get("height"))
        );
    }
}
