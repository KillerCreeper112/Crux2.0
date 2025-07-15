package killercreepr.cruxattributes.core;

import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.plugin.module.StandardModules;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxattributes.api.values.ValuesProvider;
import killercreepr.cruxattributes.core.command.CruxAttributeCommands;
import killercreepr.cruxattributes.core.component.CruxAttributeComponents;
import killercreepr.cruxattributes.core.config.Config;
import killercreepr.cruxattributes.core.config.CruxAttributesCfgHook;
import killercreepr.cruxattributes.core.listener.CruxAttributeListener;
import killercreepr.cruxattributes.core.persistence.CruxAttributesPersistence;
import killercreepr.cruxattributes.core.text.tags.CruxAttributeInstanceTags;
import killercreepr.cruxattributes.core.text.tags.CruxAttributeModifierTags;
import killercreepr.cruxattributes.core.text.tags.CruxAttributeTags;
import killercreepr.cruxattributes.core.text.tags.ItemStackTags;
import killercreepr.cruxattributes.core.values.DefaultValues;
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

    protected ValuesProvider values;

    public ValuesProvider values() {
        return values;
    }

    public void values(@NotNull ValuesProvider values) {
        this.values = values;
    }

    @Override
    public void onLoad(@NotNull CruxPlugin plugin) {
        CruxModule.super.onLoad(plugin);
        CruxSlotGroup.register();
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
            new CruxAttributeModifierTags(),
            new ItemStackTags(() -> values)
        );
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        boolean cruxConfigs = CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS);
        if(cruxConfigs){
            values(new Config(plugin, "module/attribute"));
        }else values(new DefaultValues());

        plugin.registerListeners(
            new CruxAttributeListener()
        );
        CruxAttributeCommands.register(plugin);
    }

    @Override
    public void reload(@NotNull CruxPlugin plugin) {
        CruxModule.super.reload(plugin);
        values.reload(plugin);
    }
}
