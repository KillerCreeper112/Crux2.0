package killercreepr.cruxstats.api.bukkit;

import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.cruxstats.api.stat.CruxStatHolder;
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
            //todo make statholder for offline players
            return null;
        }
        return mem.getDataHolders().get(PlayerCruxStatHolder.class, PlayerCruxStatHolder.KEY);
    }
}
