package killercreepr.cruxstructures.core.config.generation;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.core.structure.generation.center.RandomSurfaceTopAvoidTreeCenter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileRandomSurfaceTopAvoidTreeCenter extends PureYamlFileHandler<RandomSurfaceTopAvoidTreeCenter> {
    @Override
    public @Nullable RandomSurfaceTopAvoidTreeCenter deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        int maxFindValidSpaceAttempts = o.getObject(Integer.class, "max_find_valid_space", 16);
        return new RandomSurfaceTopAvoidTreeCenter(
            maxFindValidSpaceAttempts
        );
    }

}
