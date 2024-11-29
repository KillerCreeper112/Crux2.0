package killercreepr.cruxform.core;

import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.core.plugin.module.StandardModules;
import org.jetbrains.annotations.NotNull;

public class CruxFormModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_EXTERNAL;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }
}
