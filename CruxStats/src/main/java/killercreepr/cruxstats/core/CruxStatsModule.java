package killercreepr.cruxstats.core;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxstats.core.command.CruxStatsCommands;
import killercreepr.cruxstats.core.config.CruxConfigHook;
import org.jetbrains.annotations.NotNull;

public class CruxStatsModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_STATS;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onLoad(@NotNull CruxPlugin plugin) {
        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS)){
            CruxConfigHook.register();
        }
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        CruxStatsCommands.register(plugin);
    }
}
