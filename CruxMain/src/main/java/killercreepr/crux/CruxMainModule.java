package killercreepr.crux;

import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.listener.PlayerDataListener;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class CruxMainModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_MAIN;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onDisable(@NotNull CruxPlugin plugin) {
        for(EntityMemory data : EntityMemory.REGISTRY){
            data.removeDataHolders(data.value());
        }
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        Crux.log(Level.WARNING, "enable crux main");
        plugin.getServer().getPluginManager().registerEvents(new PlayerDataListener(), plugin);
        Crux.buildTickRunnable().runTaskTimer(plugin, 20L, 1L);
    }
}
