package killercreepr.cruxentities.listener;

import killercreepr.crux.api.entity.CruxEntity;
import killercreepr.crux.core.component.CruxComponents;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityComponentListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if(!CruxEntity.entity(event.getEntity()).getOrDefault(CruxComponents.INVULNERABLE, false)) return;
        event.setCancelled(true);
    }

}
