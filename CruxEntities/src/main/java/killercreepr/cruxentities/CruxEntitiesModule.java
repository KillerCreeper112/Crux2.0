package killercreepr.cruxentities;

import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.plugin.module.StandardModules;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxentities.command.CruxEntitiesCommands;
import killercreepr.cruxentities.component.CruxEntityComponents;
import killercreepr.cruxentities.config.hook.CruxEntitiesCfgHook;
import killercreepr.cruxentities.damage.type.CruxEntityDamageTypes;
import killercreepr.cruxentities.entity.MobCategory;
import killercreepr.cruxentities.handler.CruxEntitiesEntityHandler;
import killercreepr.cruxentities.listener.*;
import killercreepr.cruxentities.modelengine.ModelEngineHook;
import org.jetbrains.annotations.NotNull;

public class CruxEntitiesModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_ENTITIES;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onLoad(@NotNull CruxPlugin plugin) {
        MobCategory.register();
        CruxEntityComponents.register();
        CruxEntityDamageTypes.register();
        Crux.handlers().setEntity(new CruxEntitiesEntityHandler());
        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS)){
            new CruxEntitiesCfgHook().register();
        }
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        plugin.registerListeners(
            new EntityManager(plugin),
            new CustomEntitySoundsListener(),
            new EntityComponentListener(),
            new EntityLogicListener(),
            new EntityCombatListener(),
            new ComponentsListener()
        );
        ModelEngineHook.register(plugin);
        CruxEntitiesCommands.register(plugin);
    }
}
