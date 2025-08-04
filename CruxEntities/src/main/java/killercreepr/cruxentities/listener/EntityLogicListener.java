package killercreepr.cruxentities.listener;

import com.destroystokyo.paper.event.entity.EntityZapEvent;
import killercreepr.crux.core.persistence.CruxPersist;
import killercreepr.cruxentities.entity.CruxMob;
import killercreepr.cruxentities.entity.CruxMobMountable;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityMountEvent;
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

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onEntityMount(EntityMountEvent event) {
        var entity = event.getEntity();
        if(entity instanceof HumanEntity) return;
        var cruxMob = CruxMob.get(entity);
        if(cruxMob == null) return;
        if(!(cruxMob instanceof CruxMobMountable mount) || !mount.canMount(entity, event.getMount())){
            if(CruxPersist.ALLOW_MOUNT.get(entity, false)) return;
            if(CruxPersist.ALLOW_MOUNT_TEMP.get(entity, false)){
                CruxPersist.ALLOW_MOUNT_TEMP.remove(entity);
                return;
            }
            event.setCancelled(true);
        }
    }

}
