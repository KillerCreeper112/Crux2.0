package killercreepr.cruxstructures.config.structure;

import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxstructures.structure.impl.SurfaceCenter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileSurfaceCenter extends PureYamlFileHandler<SurfaceCenter> {
    @Override
    public @Nullable SurfaceCenter deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        return new SurfaceCenter();
    }

}
