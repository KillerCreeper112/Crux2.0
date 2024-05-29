package killercreepr.cruxpotion;

import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxpotion.config.Config;
import killercreepr.cruxpotion.data.PotionHolder;
import killercreepr.cruxpotion.listener.PotionListener;
import killercreepr.cruxpotion.persistence.PotionPersistTags;
import killercreepr.cruxpotion.potions.ActivePotion;
import killercreepr.cruxpotion.potions.InflictedPotion;
import killercreepr.cruxpotion.potions.inflictor.BlockInflictor;
import killercreepr.cruxpotion.potions.inflictor.EntityInflictor;
import killercreepr.cruxpotion.potions.inflictor.PotionInflictor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;

public class CruxPotion implements CruxModule {
    public static final String NAMESPACE = "CruxPotion";
    protected static Config CONFIG;
    public static Config cfg(){ return CONFIG; }
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    protected Config config;
    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        config = new Config(plugin, "config");
        CONFIG = config;

        EntityMemory.registerFunction(plugin, e -> e.getHolders().register(new PotionHolder(e)));
        ConfigurationSerialization.registerClass(BlockInflictor.class);
        ConfigurationSerialization.registerClass(EntityInflictor.class);
        ConfigurationSerialization.registerClass(ActivePotion.class);
        ConfigurationSerialization.registerClass(PotionInflictor.class);
        ConfigurationSerialization.registerClass(InflictedPotion.class);

        //reload configs
        plugin.registerListeners(
                new PotionListener(plugin, config)
        );

        PotionPersistTags.register();
    }

    @Override
    public void reload() {
        config.setup();
    }
}
