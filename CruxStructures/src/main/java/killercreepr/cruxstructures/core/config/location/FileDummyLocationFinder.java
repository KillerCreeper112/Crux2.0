package killercreepr.cruxstructures.core.config.location;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.api.location.LocationFinder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileDummyLocationFinder extends PureYamlFileHandler<LocationFinder.Dummy> {
    @Override
    public @Nullable LocationFinder.Dummy deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        return new LocationFinder.Dummy();
    }
}
