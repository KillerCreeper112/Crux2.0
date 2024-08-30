package killercreepr.cruxstructures.config.generation;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.structure.generation.center.RandomSurfaceCenter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileRandomSurfaceCenter extends PureYamlFileHandler<RandomSurfaceCenter> {
    @Override
    public @Nullable RandomSurfaceCenter deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        return new RandomSurfaceCenter();
    }

}
