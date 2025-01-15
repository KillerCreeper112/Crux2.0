package killercreepr.cruxstatistics.api.bukkit;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.cruxstatistics.api.statistic.CruxStatisticHolder;
import killercreepr.cruxstatistics.core.statistic.DataStatisticHolder;
import killercreepr.cruxstatistics.core.statistic.PlayerCruxStatisticHolder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface BukkitStatisticHolder {
    static CruxStatisticHolder statisticHolder(@NotNull Entity p){
        return EntityMemory.getOrCreateDataHolder(p, PlayerCruxStatisticHolder.class);
    }

    static CruxStatisticHolder statisticHolder(@NotNull UUID uuid){
        EntityMemory mem = EntityMemory.get(uuid);
        if(mem == null){
            DataStatisticHolder stats = new DataStatisticHolder(uuid);
            stats.load();
            return stats;
        }
        return mem.getDataHolders().get(PlayerCruxStatisticHolder.class, PlayerCruxStatisticHolder.KEY);
    }
}