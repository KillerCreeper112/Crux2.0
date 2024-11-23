package killercreepr.cruxconfig;

import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.plugin.module.StandardModules;
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
