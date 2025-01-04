package killercreepr.cruxstats.api.bukkit;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.cruxstats.api.stat.CruxStatHolder;
import killercreepr.cruxstats.core.stat.DataStatHolder;
import killercreepr.cruxstats.core.stat.PlayerCruxStatHolder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface BukkitStatHolder {
    static CruxStatHolder holder(@NotNull Player p){
        return EntityMemory.getOrCreateDataHolder(p, PlayerCruxStatHolder.class);
    }

    static CruxStatHolder holder(@NotNull UUID uuid){
        EntityMemory mem = EntityMemory.get(uuid);
        if(mem == null){
            DataStatHolder stats = new DataStatHolder(uuid);
            stats.load();
            return stats;
        }
        return mem.getDataHolders().get(PlayerCruxStatHolder.class, PlayerCruxStatHolder.KEY);
    }
}