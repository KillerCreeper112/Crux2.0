package killercreepr.cruxentities;

import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.plugin.module.StandardModules;
import killercreepr.cruxentities.command.CruxEntitiesCommands;
import killercreepr.cruxentities.entity.MobCategory;
import killercreepr.cruxentities.handler.CruxEntitiesEntityHandler;
import killercreepr.cruxentities.listener.CustomEntitySoundsListener;
import killercreepr.cruxentities.listener.EntityManager;
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
        Crux.handlers().setEntity(new CruxEntitiesEntityHandler());
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        plugin.registerListeners(
            new EntityManager(plugin),
            new CustomEntitySoundsListener()
        );
        ModelEngineHook.register(plugin);
        CruxEntitiesCommands.register(plugin);
    }
}
