package killercreepr.cruxpotions;

import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxpotions.command.CruxPotionCommands;
import killercreepr.cruxpotions.config.Config;
import killercreepr.cruxpotions.data.PotionHolder;
import killercreepr.cruxpotions.listener.PotionListener;
import killercreepr.cruxpotions.persistence.PotionPersistTags;
import killercreepr.cruxpotions.potions.ActivePotion;
import killercreepr.cruxpotions.potions.InflictedPotion;
import killercreepr.cruxpotions.potions.inflictor.BlockInflictor;
import killercreepr.cruxpotions.potions.inflictor.EntityInflictor;
import killercreepr.cruxpotions.potions.inflictor.PotionInflictor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;

public class CruxPotionsModule implements CruxModule {
    public static final String NAMESPACE = "CruxPotions";
    protected static Config CONFIG;
    public static Config cfg(){ return CONFIG; }
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    protected Config config;
    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        config = new Config(plugin, "module/potion");
        CONFIG = config;

        EntityMemory.registerFunction(plugin, e -> e.getHolders().register(new PotionHolder(e)));
        ConfigurationSerialization.registerClass(BlockInflictor.class);
        ConfigurationSerialization.registerClass(EntityInflictor.class);
        ConfigurationSerialization.registerClass(ActivePotion.class);
        ConfigurationSerialization.registerClass(PotionInflictor.class);
        ConfigurationSerialization.registerClass(InflictedPotion.class);

        plugin.registerListeners(
                new PotionListener(plugin, config)
        );

        PotionPersistTags.register();
        CruxPotionCommands.register(plugin);
    }

    @Override
    public void reload(@NotNull CruxPlugin plugin) {
        config.setup();
    }
}
