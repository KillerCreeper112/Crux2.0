package killercreepr.cruxconfig;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import org.jetbrains.annotations.NotNull;

public class CruxConfigsModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_CONFIGS;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }
}
