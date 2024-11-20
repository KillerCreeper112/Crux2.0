package killercreepr.cruxstats.core;

import killercreepr.crux.core.Crux;
import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.core.plugin.module.StandardModules;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.api.text.tags.TagParser;
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
