package killercreepr.cruxexternal;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import org.jetbrains.annotations.NotNull;

public class CruxExternalModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_EXTERNAL;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {

    }
}
