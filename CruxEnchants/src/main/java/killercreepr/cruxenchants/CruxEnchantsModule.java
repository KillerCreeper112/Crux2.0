package killercreepr.cruxenchants;

import killercreepr.crux.Crux;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxenchants.command.CruxEnchantCommands;
import killercreepr.cruxenchants.config.Config;
import killercreepr.cruxenchants.tags.EnchantLoreTag;
import killercreepr.cruxenchants.values.DefaultValues;
import killercreepr.cruxenchants.values.ValuesProvider;
import org.jetbrains.annotations.NotNull;

public class CruxEnchantsModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_ENCHANTS;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    protected ValuesProvider values;

    public ValuesProvider values() {
        return values;
    }

    public void values(@NotNull ValuesProvider values) {
        this.values = values;
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        if(CruxRegistries.MODULES.containsKey("CruxConfigs")){
            values(new Config(plugin, "module/enchant"));
        }else values(new DefaultValues());

        CruxEnchantCommands.register(plugin);
        Crux.TAGS.register(new EnchantLoreTag(values.enchantsTagFormat()));
    }

    @Override
    public void reload(@NotNull CruxPlugin plugin) {
        values.reload(plugin);
    }
}
