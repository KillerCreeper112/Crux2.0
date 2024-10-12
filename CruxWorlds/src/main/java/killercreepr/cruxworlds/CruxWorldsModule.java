package killercreepr.cruxworlds;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxworlds.config.CruxConfigsHook;
import org.jetbrains.annotations.NotNull;

public class CruxWorldsModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_WORLDS;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onLoad(@NotNull CruxPlugin plugin) {
        CruxModule.super.onLoad(plugin);
        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS)){
            CruxConfigsHook.register();
        }
    }
}
