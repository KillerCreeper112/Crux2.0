package killercreepr.cruxattributes.core;

import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.plugin.module.StandardModules;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxattributes.core.command.CruxAttributeCommands;
import killercreepr.cruxattributes.core.component.CruxAttributeComponents;
import killercreepr.cruxattributes.core.config.CruxAttributesCfgHook;
import killercreepr.cruxattributes.core.listener.CruxAttributeListener;
import killercreepr.cruxattributes.core.persistence.CruxAttributesPersistence;
import killercreepr.cruxattributes.core.text.tags.CruxAttributeInstanceTags;
import killercreepr.cruxattributes.core.text.tags.CruxAttributeModifierTags;
import killercreepr.cruxattributes.core.text.tags.CruxAttributeTags;
import org.jetbrains.annotations.NotNull;

public class CruxAttributesModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_ATTRIBUTES;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    public CruxAttributesModule() {
        CruxAttributesPersistence.register();
    }

    @Override
    public void onLoad(@NotNull CruxPlugin plugin) {
        CruxModule.super.onLoad(plugin);
        CruxAttributeComponents.register();
        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS)){
            CruxAttributesCfgHook.onLoad();
        }
        registerTags(Crux.tags());
    }

    public void registerTags(TagParser tags){
        tags.register(
            new CruxAttributeTags(),
            new CruxAttributeInstanceTags(),
            new CruxAttributeModifierTags()
        );
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        plugin.registerListeners(
            new CruxAttributeListener()
        );
        CruxAttributeCommands.register(plugin);
    }
}
