package killercreepr.cruxattributes;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxattributes.command.CruxAttributeCommands;
import killercreepr.cruxattributes.listener.CruxAttributeListener;
import killercreepr.cruxattributes.persistence.CruxAttributesPersistence;
import org.jetbrains.annotations.NotNull;

public class CruxAttributesModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_ATTRIBUTES;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    public CruxAttributesModule() {
        CruxAttributesPersistence.register();
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        plugin.registerListeners(
                new CruxAttributeListener()
        );
        CruxAttributeCommands.register(plugin);
    }
}
