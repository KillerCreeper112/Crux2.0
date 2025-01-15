package killercreepr.cruxstatistics.core;

import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.plugin.module.StandardModules;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxstatistics.core.config.CruxConfigHook;
import killercreepr.cruxstatistics.core.tags.object.StatisticPlayerTags;
import org.jetbrains.annotations.NotNull;

public class CruxStatisticsModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_STATISTICS;
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
        tags.register(new StatisticPlayerTags());
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
    }
}
