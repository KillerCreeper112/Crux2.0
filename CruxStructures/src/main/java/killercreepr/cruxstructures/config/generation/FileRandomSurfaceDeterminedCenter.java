package killercreepr.cruxstructures.config.generation;

import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.structure.generation.center.RandomSurfaceCenter;
import killercreepr.cruxstructures.structure.generation.center.RandomSurfaceDeterminedCenter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileRandomSurfaceDeterminedCenter extends PureYamlFileHandler<RandomSurfaceCenter> {
    @Override
    public @Nullable RandomSurfaceCenter deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
        int maxAttempts = o.getObject(Integer.class,"max_attempts", 16);
        NumberProvider yLimit = registry.deserializeFromFile(NumberProvider.class, o.get("y_limit"));
        return new RandomSurfaceDeterminedCenter(maxAttempts, yLimit);
    }

}
