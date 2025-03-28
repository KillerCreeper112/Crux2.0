package killercreepr.cruxstructures.core.config.generation;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.core.structure.generation.center.RandomAnywhereCenter;
import killercreepr.cruxstructures.core.structure.generation.center.RandomSurfaceTopCenter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileRandomAnywhereCenter extends PureYamlFileHandler<RandomAnywhereCenter> {
    @Override
    public @Nullable RandomAnywhereCenter deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        NumberProvider yRange = null;
        if(e instanceof FileObject o){
            yRange = context.getRegistry().deserializeFromFile(NumberProvider.class, o.get("y_range"));
        }
        if(yRange == null) yRange = NumberProvider.uniform(-64, 320);
        return new RandomAnywhereCenter(yRange);
    }

}
