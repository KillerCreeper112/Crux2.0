package killercreepr.crux;

import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.listener.PlayerDataListener;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import org.jetbrains.annotations.NotNull;

public class CruxMainModule implements CruxModule {
    @Override
    public @NotNull String name() {
        return "CruxMain";
    }

    @Override
    public void onDisable(@NotNull CruxPlugin plugin) {
        for(EntityMemory data : EntityMemory.REGISTRY){
            data.removeDataHolders(data.value());
        }
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new PlayerDataListener(), plugin);
        Crux.buildTickRunnable().runTaskTimer(plugin, 20L, 1L);
    }
}
