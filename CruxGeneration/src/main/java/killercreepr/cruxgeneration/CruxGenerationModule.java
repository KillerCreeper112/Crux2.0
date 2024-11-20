package killercreepr.cruxgeneration;

import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.core.plugin.module.StandardModules;
import org.jetbrains.annotations.NotNull;

public class CruxGenerationModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_GENERATION;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }
}
