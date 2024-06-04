package killercreepr.cruxattributes;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxattributes.listener.CruxAttributeListener;
import org.jetbrains.annotations.NotNull;

public class CruxAttributesModule implements CruxModule {
    public static final String NAMESPACE = "CruxAttributes";
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        plugin.registerListeners(
                new CruxAttributeListener()
        );
    }
}
