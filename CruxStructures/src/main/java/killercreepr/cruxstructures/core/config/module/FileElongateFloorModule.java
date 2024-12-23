package killercreepr.cruxstructures.core.config.module;

import killercreepr.crux.api.block.CruxBlockWrapper;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.core.structure.module.ElongateFloorModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileElongateFloorModule extends PureYamlFileHandler<ElongateFloorModule> {
    @Override
    public @Nullable ElongateFloorModule deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
        return new ElongateFloorModule(
            registry.deserializeFromFile(CruxBlockWrapper.class, o.get("block"))
        );
    }
}
