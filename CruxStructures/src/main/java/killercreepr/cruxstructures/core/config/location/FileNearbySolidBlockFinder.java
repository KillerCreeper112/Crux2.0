package killercreepr.cruxstructures.core.config.location;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.core.location.NearbySolidBlockFinder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileNearbySolidBlockFinder extends PureYamlFileHandler<NearbySolidBlockFinder> {
    @Override
    public @Nullable NearbySolidBlockFinder deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        NumberProvider maxYCheck = registry.deserializeFromFile(NumberProvider.class, o.get("max_y_check"));
        if(maxYCheck == null) maxYCheck = NumberProvider.constant(16);
        return new NearbySolidBlockFinder(maxYCheck);
    }
}
