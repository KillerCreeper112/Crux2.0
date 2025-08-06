package killercreepr.crux.core.listener;

import killercreepr.crux.core.persistence.CruxPersist;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityMechanicsListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        Entity e = event.getEntity();
        if(!CruxPersist.DROP_EXPERIENCE.get(e, true)){
            event.setDroppedExp(0);
        }
        if(!CruxPersist.DROP_ITEMS.get(e, true)){
            event.getDrops().clear();
        }
    }

}
