package killercreepr.cruxentities;

import com.ticxo.modelengine.api.events.ModelDismountEvent;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxentities.command.CruxEntitiesCommands;
import killercreepr.cruxentities.listener.CustomEntitySoundsListener;
import killercreepr.cruxentities.listener.EntityManager;
import org.jetbrains.annotations.NotNull;

public class CruxEntitiesModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_ENTITIES;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        plugin.registerListeners(
            new EntityManager(plugin),
            new CustomEntitySoundsListener()
        );
        CruxEntitiesCommands.register(plugin);
    }
}
