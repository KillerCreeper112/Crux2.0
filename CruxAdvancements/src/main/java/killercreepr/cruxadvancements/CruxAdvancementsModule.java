package killercreepr.cruxadvancements;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxadvancements.config.CruxConfigHook;
import killercreepr.cruxadvancements.crazy.CrazyAdvancementsHook;
import org.jetbrains.annotations.NotNull;

public class CruxAdvancementsModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_ADVANCEMENTS;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onLoad(@NotNull CruxPlugin plugin) {
        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS)){
            CruxConfigHook.registerHandlers();
            if(plugin.getServer().getPluginManager().getPlugin("CrazyAdvancementsAPI") != null){
                CrazyAdvancementsHook.registerHandlers();
            }
        }
    }
}
