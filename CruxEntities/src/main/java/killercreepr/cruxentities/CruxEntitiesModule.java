package killercreepr.cruxentities;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxentities.command.CruxEntitiesCommands;
import killercreepr.cruxentities.listener.EntityManager;
import org.jetbrains.annotations.NotNull;

public class CruxEntitiesModule implements CruxModule {
    public static final String NAMESPACE = "CruxEntities";
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        plugin.registerListeners(
            new EntityManager(plugin)
        );

        CruxEntitiesCommands.register(plugin);
    }
}
