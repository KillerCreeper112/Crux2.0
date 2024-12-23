package killercreepr.cruxstructures.core.config.generation;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.core.structure.generation.center.RandomSurfaceTopCenter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileRandomSurfaceTopCenter extends PureYamlFileHandler<RandomSurfaceTopCenter> {
    @Override
    public @Nullable RandomSurfaceTopCenter deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        return new RandomSurfaceTopCenter();
    }

}
