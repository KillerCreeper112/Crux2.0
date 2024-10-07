package killercreepr.cruxadvancements;

import killercreepr.crux.Crux;
import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.data.entity.PlayerMemory;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.crux.tags.TagParser;
import killercreepr.cruxadvancements.command.AdvancementCommands;
import killercreepr.cruxadvancements.config.CruxConfigHook;
import killercreepr.cruxadvancements.crazy.CrazyAdvancementsHook;
import killercreepr.cruxadvancements.data.entity.AdvancementHolder;
import killercreepr.cruxadvancements.hook.menu.CruxMenuHook;
import killercreepr.cruxadvancements.listener.AdvancementGrantListener;
import killercreepr.cruxadvancements.listener.ObjectiveListener;
import killercreepr.cruxadvancements.tags.CrazyAdvancementTags;
import killercreepr.cruxadvancements.tags.CruxAdvancementIconTags;
import killercreepr.cruxadvancements.tags.CruxAdvancementManagerTags;
import killercreepr.cruxmenus.CruxMenusModule;
import org.jetbrains.annotations.NotNull;

public class CruxAdvancementsModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_ADVANCEMENTS;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onLoad(@NotNull CruxPlugin plugin) {
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
        plugin.registerListeners(
            new ObjectiveListener(),
            new AdvancementGrantListener()
        );
    }

    public void registerTags(TagParser tags){
        tags.register(
            new CrazyAdvancementTags(),
            new CruxAdvancementIconTags(),
            new CruxAdvancementManagerTags()
        );
    }
}
