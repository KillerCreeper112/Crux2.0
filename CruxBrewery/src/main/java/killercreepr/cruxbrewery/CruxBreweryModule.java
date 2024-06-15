package killercreepr.cruxbrewery;

import io.netty.util.internal.UnstableApi;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxbrewery.listener.BrewingStandListener;
import org.jetbrains.annotations.NotNull;

@UnstableApi
@Deprecated(since = "CruxBrewery has not had a good implementation thought through yet.")
public class CruxBreweryModule implements CruxModule {
    public static final String NAMESPACE = "CruxBrewery";
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        plugin.registerListeners(
            new BrewingStandListener(plugin)
        );
    }
}
