package killercreepr.cruxstats.core;

import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.data.entity.PlayerMemory;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxstats.core.command.CruxStatsCommands;
import killercreepr.cruxstats.core.stat.PlayerCruxStatHolder;
import org.jetbrains.annotations.NotNull;

public class CruxStatsModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_STATS;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onLoad(@NotNull CruxPlugin plugin) {
        //todo move to cruxcore
        EntityMemory.registerFunction(plugin, (m) ->{
            if(!(m instanceof PlayerMemory mem)) return;
            mem.getDataHolders().register(new PlayerCruxStatHolder(mem));
        });
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        CruxStatsCommands.register(plugin);
    }
}
