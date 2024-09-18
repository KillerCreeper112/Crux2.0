package killercreepr.cruxconfig;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxconfig.config.bukkit.handler.impl.item.component.persistence.StandardDynamicPersistTags;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import org.jetbrains.annotations.NotNull;

public class CruxConfigsModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_CONFIGS;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onLoad(@NotNull CruxPlugin plugin) {
        StandardDynamicPersistTags.register(CfgRegistries.DYNAMIC_PERSIST_TAG_PARSER);
        CruxModule.super.onLoad(plugin);
    }
}
