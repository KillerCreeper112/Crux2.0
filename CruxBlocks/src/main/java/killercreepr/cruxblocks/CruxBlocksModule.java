package killercreepr.cruxblocks;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxblocks.listener.CustomBlocksListener;
import org.jetbrains.annotations.NotNull;

public class CruxBlocksModule implements CruxModule {
    public static final String NAMESPACE = "CruxBlocks";
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        plugin.registerListeners(
            new CustomBlocksListener()
        );
    }
}
