package killercreepr.cruxentities.listener;

import com.destroystokyo.paper.event.entity.EntityZapEvent;
import killercreepr.cruxentities.entity.CruxMob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTransformEvent;

public class EntityLogicListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityTransform(EntityTransformEvent event) {
        if(CruxMob.is(event.getEntity())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityZap(EntityZapEvent event) {
        if(CruxMob.is(event.getEntity())) event.setCancelled(true);
    }
}
