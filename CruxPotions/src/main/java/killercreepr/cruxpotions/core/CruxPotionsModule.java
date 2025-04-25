package killercreepr.cruxpotions.core;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.plugin.module.StandardModules;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxconfig.config.common.handler.AutoFileHandler;
import killercreepr.cruxconfig.config.common.handler.AutoFileOptions;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxitems.core.registries.CruxItemRegistries;
import killercreepr.cruxpotions.api.potion.StoredPotion;
import killercreepr.cruxpotions.api.potion.inflictor.PotionInflictor;
import killercreepr.cruxpotions.api.values.ValuesProvider;
import killercreepr.cruxpotions.core.command.CruxPotionCommands;
import killercreepr.cruxpotions.core.config.Config;
import killercreepr.cruxpotions.core.config.CruxPotionCfgHandler;
import killercreepr.cruxpotions.core.config.handler.FileStoredPotion;
import killercreepr.cruxpotions.core.entity.memory.SimplePotionHolder;
import killercreepr.cruxpotions.core.item.PotionItemUpdater;
import killercreepr.cruxpotions.core.listener.PlayerDataListener;
import killercreepr.cruxpotions.core.listener.PotionListener;
import killercreepr.cruxpotions.core.persistence.CruxPotionsPersistence;
import killercreepr.cruxpotions.core.persistence.PotionPersistTags;
import killercreepr.cruxpotions.core.potions.inflictor.BlockInflictor;
import killercreepr.cruxpotions.core.potions.inflictor.EntityInflictor;
import killercreepr.cruxpotions.core.tags.PlayerTags;
import killercreepr.cruxpotions.core.tags.PotionsLoreTag;
import killercreepr.cruxpotions.core.values.DefaultValues;
import org.jetbrains.annotations.NotNull;

public class CruxPotionsModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_POTIONS;

    public CruxPotionsModule() {
        CruxPotionsPersistence.register();
    }

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
    public void onLoad(@NotNull CruxPlugin plugin) {
        CruxPotionCommands.register(plugin);
        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS)){
            CruxPotionCfgHandler.POTION_INFLICTOR.register(BlockInflictor.ID, new AutoFileHandler<>(BlockInflictor.class));
            CruxPotionCfgHandler.POTION_INFLICTOR.register(BlockInflictor.ID, new AutoFileHandler<>(EntityInflictor.class,
                AutoFileOptions.builder().disabledFields(field -> field.getName().equalsIgnoreCase("reference")).build()));
            CfgRegistries.SIMPLE_REGISTRY.forEach(reg ->{
                reg.registerFileHandler(PotionInflictor.class, CruxPotionCfgHandler.POTION_INFLICTOR);
                reg.registerFileHandler(StoredPotion.class, new FileStoredPotion());
            });
        }
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        boolean cruxConfigs = CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS);
        if(cruxConfigs){
            values(new Config(plugin, "module/potion"));
        }else values(new DefaultValues());

        EntityMemory.registerFunction(plugin, e -> e.getDataHolders().register(new SimplePotionHolder(e)));

        plugin.registerListeners(
            new PotionListener(plugin, values)
        );
        if(cruxConfigs) plugin.registerListeners(new PlayerDataListener(plugin, values));

        PotionPersistTags.register();
        //CruxPotionCommands.register(plugin);
        Crux.tags().register(
            new PotionsLoreTag(values.potionsFormat()),
            new PlayerTags());

        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_ITEMS)){
            CruxItemRegistries.ITEM_UPDATERS.register(3, new PotionItemUpdater());
        }
    }

    @Override
    public void reload(@NotNull CruxPlugin plugin) {
        values.reload(plugin);
    }
}
