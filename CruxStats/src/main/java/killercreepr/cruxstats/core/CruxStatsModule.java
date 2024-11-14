package killercreepr.cruxstats.core;

import killercreepr.crux.Crux;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.crux.tags.TagParser;
import killercreepr.cruxstats.core.command.CruxStatsCommands;
import killercreepr.cruxstats.core.config.CruxConfigHook;
import killercreepr.cruxstats.core.tags.object.StatPlayerTags;
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
        registerTags(Crux.tags());
    }

    public void registerTags(TagParser tags){
        tags.register(new StatPlayerTags());
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        CruxStatsCommands.register(plugin);
    }
}
