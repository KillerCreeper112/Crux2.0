package killercreepr.cruxtickables.core;

import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.plugin.module.StandardModules;
import killercreepr.cruxtickables.core.component.CruxTickableComponents;
import killercreepr.cruxtickables.core.listener.TickableListener;
import org.jetbrains.annotations.NotNull;

public class CruxTickablesModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_TICKABLES;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onLoad(@NotNull CruxPlugin plugin) {
        CruxTickableComponents.register();
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        plugin.registerListeners(new TickableListener());
    }
}
