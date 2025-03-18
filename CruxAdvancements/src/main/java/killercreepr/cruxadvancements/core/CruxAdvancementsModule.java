package killercreepr.cruxadvancements.core;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.entity.memory.PlayerMemory;
import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.plugin.module.StandardModules;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxadvancements.api.values.ValuesProvider;
import killercreepr.cruxadvancements.core.command.AdvancementCommands;
import killercreepr.cruxadvancements.core.config.Config;
import killercreepr.cruxadvancements.core.config.CruxConfigHook;
import killercreepr.cruxadvancements.core.entity.memory.AdvancementHolder;
import killercreepr.cruxadvancements.core.hook.menu.CruxMenuHook;
import killercreepr.cruxadvancements.core.listener.AdvancementGrantListener;
import killercreepr.cruxadvancements.core.listener.ObjectiveListener;
import killercreepr.cruxadvancements.core.listener.PlayerCraftItemListener;
import killercreepr.cruxadvancements.core.stat.AdvancementStats;
import killercreepr.cruxadvancements.core.tags.*;
import killercreepr.cruxadvancements.core.values.DefaultValues;
import killercreepr.cruxadvancements.crazy.advancement.CrazyAdvancementsHook;
import killercreepr.cruxmenus.CruxMenusModule;
import org.jetbrains.annotations.NotNull;

public class CruxAdvancementsModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_ADVANCEMENTS;
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
        AdvancementStats.register();
        AdvancementCommands.register(plugin);
        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS)){
            CruxConfigHook.load();
            CruxConfigHook.registerHandlers();
            if(plugin.getServer().getPluginManager().getPlugin("CrazyAdvancementsAPI") != null){
                CrazyAdvancementsHook.registerHandlers();
            }
            if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_MENUS)){
                CruxMenuHook.register(CruxRegistries.MODULES.getModuleOrThrow(CruxMenusModule.class));
            }
        }
        EntityMemory.registerFunction(plugin, mem ->{
            if(!(mem instanceof PlayerMemory data)) return;
            data.getDataHolders().register(new AdvancementHolder(data, plugin));
        });
        registerTags(Crux.tags());
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        boolean cruxConfigs = CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS);
        if(cruxConfigs){
            values(new Config(plugin, "module/advancement"));
        }else values(new DefaultValues());

        plugin.registerListeners(
            new ObjectiveListener(),
            new AdvancementGrantListener(),
            new PlayerCraftItemListener()
        );
    }

    @Override
    public void reload(@NotNull CruxPlugin plugin) {
        CruxModule.super.reload(plugin);
        values.reload(plugin);
    }

    public void registerTags(TagParser tags){
        tags.register(
            new CrazyAdvancementTags(),
            new CruxAdvancementIconTags(),
            new CruxAdvancementManagerTags(),
            new AdvancementPlayerTags(),
            new TrackedAdvancementTags()
        );
    }
}
