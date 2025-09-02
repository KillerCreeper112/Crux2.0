package killercreepr.cruxentities.listener;

import com.destroystokyo.paper.event.entity.EntityZapEvent;
import killercreepr.crux.api.entity.CruxEntity;
import killercreepr.crux.api.event.CruxEntityDeathEvent;
import killercreepr.crux.core.persistence.CruxPersist;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxentities.component.CruxEntityComponents;
import killercreepr.cruxentities.component.LaunchDrops;
import killercreepr.cruxentities.component.PreventMerge;
import killercreepr.cruxentities.entity.CruxMob;
import killercreepr.cruxentities.entity.CruxMobMountable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityMountEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.ItemMergeEvent;

public class EntityLogicListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityTransform(EntityTransformEvent event) {
        if(CruxMob.is(event.getEntity())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        if(event.getDrops().isEmpty()) return;

        Entity e = event.getEntity();
        CruxEntity crux = CruxEntity.entity(e);
        LaunchDrops launchDrops = crux.get(CruxEntityComponents.LAUNCH_DROPS);
        if(launchDrops != null){
            launchDrops.launchDrops(e.getLocation(), event.getDrops());
            event.getDrops().clear();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onItemMerge(ItemMergeEvent event) {
        Entity e = event.getEntity();
        CruxEntity crux = CruxEntity.entity(e);
        PreventMerge preventMerge = crux.get(CruxEntityComponents.PREVENT_MERGE);
        if(preventMerge != null){
            if(CruxMath.hasOccurredWithin(preventMerge.time(), preventMerge.ticks())){
                event.setCancelled(true);
                return;
            }
        }
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
