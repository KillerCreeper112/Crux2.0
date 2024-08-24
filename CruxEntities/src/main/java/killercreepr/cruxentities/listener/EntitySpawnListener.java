package killercreepr.cruxentities.listener;

import killercreepr.cruxentities.persistence.CruxEntitiesPersist;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EntitySpawnListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Entity e = event.getEntity();
        if(CruxEntitiesPersist.SPAWN_REASON.get(e,null) != null) return;
        CruxEntitiesPersist.SPAWN_REASON.set(e, event.getSpawnReason().toString().toLowerCase());
    }
}
