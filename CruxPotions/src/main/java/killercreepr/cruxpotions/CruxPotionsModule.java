package killercreepr.cruxpotions;

import killercreepr.crux.Crux;
import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxitems.registries.CruxItemRegistries;
import killercreepr.cruxpotions.command.CruxPotionCommands;
import killercreepr.cruxpotions.config.Config;
import killercreepr.cruxpotions.data.PotionHolder;
import killercreepr.cruxpotions.item.PotionItemUpdater;
import killercreepr.cruxpotions.listener.PlayerDataListener;
import killercreepr.cruxpotions.listener.PotionListener;
import killercreepr.cruxpotions.persistence.PotionPersistTags;
import killercreepr.cruxpotions.potions.ActivePotion;
import killercreepr.cruxpotions.potions.InflictedPotion;
import killercreepr.cruxpotions.potions.inflictor.BlockInflictor;
import killercreepr.cruxpotions.potions.inflictor.EntityInflictor;
import killercreepr.cruxpotions.potions.inflictor.PotionInflictor;
import killercreepr.cruxpotions.tags.PotionsLoreTag;
import killercreepr.cruxpotions.values.DefaultValues;
import killercreepr.cruxpotions.values.ValuesProvider;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;

public class CruxPotionsModule implements CruxModule {
    public static final String NAMESPACE = "CruxPotions";
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
        boolean cruxConfigs = CruxRegistries.MODULES.containsKey("CruxConfigs");
        if(cruxConfigs){
            values(new Config(plugin, "module/enchant"));
        }else values(new DefaultValues());

        EntityMemory.registerFunction(plugin, e -> e.getHolders().register(new PotionHolder(e)));
        ConfigurationSerialization.registerClass(BlockInflictor.class);
        ConfigurationSerialization.registerClass(EntityInflictor.class);
        ConfigurationSerialization.registerClass(ActivePotion.class);
        ConfigurationSerialization.registerClass(PotionInflictor.class);
        ConfigurationSerialization.registerClass(InflictedPotion.class);

        plugin.registerListeners(
                new PotionListener(plugin, values)
        );
        if(cruxConfigs) plugin.registerListeners(new PlayerDataListener(plugin, values));

        PotionPersistTags.register();
        CruxPotionCommands.register(plugin);
        Crux.TAGS.register(new PotionsLoreTag(values.potionsFormat()));

        if(CruxRegistries.MODULES.containsKey("CruxItems")){
            CruxItemRegistries.ITEM_UPDATERS.register(3, new PotionItemUpdater());
        }
    }

    @Override
    public void reload(@NotNull CruxPlugin plugin) {
        values.reload(plugin);
    }
}
