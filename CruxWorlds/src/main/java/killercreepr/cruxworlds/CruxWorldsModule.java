package killercreepr.cruxworlds;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import org.jetbrains.annotations.NotNull;

public class CruxWorldsModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_WORLDS;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }
}
