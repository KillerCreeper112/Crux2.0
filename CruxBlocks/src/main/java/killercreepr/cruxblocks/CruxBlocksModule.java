package killercreepr.cruxblocks;

import killercreepr.crux.Crux;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.CruxRegistries;
import org.jetbrains.annotations.NotNull;

public class CruxBlocksModule implements CruxModule {
    public static final String NAMESPACE = "CruxBlocks";
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {

    }
}
