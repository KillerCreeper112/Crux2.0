package killercreepr.cruxworlds.core;

import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.plugin.module.StandardModules;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxworlds.core.component.CruxWorldsComponents;
import killercreepr.cruxworlds.core.config.CruxConfigsHook;
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
        CruxWorldsComponents.register();
    }
}
