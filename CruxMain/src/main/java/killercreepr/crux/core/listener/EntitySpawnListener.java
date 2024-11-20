package killercreepr.crux.core.listener;

import killercreepr.crux.core.persistence.CruxPersist;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EntitySpawnListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Entity e = event.getEntity();
        if(CruxPersist.SPAWN_REASON.get(e,null) != null) return;
        CruxPersist.SPAWN_REASON.set(e, event.getSpawnReason().toString().toLowerCase());
    }
}
