package killercreepr.cruxitems;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import org.jetbrains.annotations.NotNull;

public class CruxItemsModule implements CruxModule {
    public static final String NAMESPACE = "CruxItems";
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
    }
}
