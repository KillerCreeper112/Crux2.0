package killercreepr.cruxattributes.core;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxattributes.core.command.CruxAttributeCommands;
import killercreepr.cruxattributes.core.listener.CruxAttributeListener;
import killercreepr.cruxattributes.core.persistence.CruxAttributesPersistence;
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
