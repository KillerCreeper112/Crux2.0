package killercreepr.cruxentities.listener;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.event.CruxEntityDamageEvent;
import killercreepr.crux.core.persistence.CruxPersist;
import killercreepr.cruxentities.entity.memory.PreventVanillaAttackHolder;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityCombatListener implements Listener {
    /*@EventHandler(ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile proj = event.getEntity();
        Entity hit = event.getHitEntity();
        if(hit == null) return;
        CruxAttributeInstance dmgInstance = CruxAttribute.getInstance(proj, CruxAttribute.ATTACK_DAMAGE);
        if(dmgInstance != null){
            EntityDamager.entityDamager(hit, proj).attack();
            return;
        }
        CruxAttributeInstance kbInstance = CruxAttribute.getInstance(proj, CruxAttribute.ATTACK_KNOCKBACK);
        if(kbInstance != null){
            EntityDamager.entityDamager(hit, proj).attack();
            return;
        }
        CruxAttributeInstance kbUpInstance = CruxAttribute.getInstance(proj, CruxAttribute.ATTACK_KNOCKBACK_UP);
        if(kbUpInstance != null){
            EntityDamager.entityDamager(hit, proj).attack();
        }
    }*/

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCruxEntityDamage(CruxEntityDamageEvent event) {
        if(!(event.getDamager() instanceof Mob mob)) return;
        if(!CruxPersist.DISABLE_VANILLA_ATTACK.get(mob, false)) return;
        var data = EntityMemory.getOrCreateDataHolder(mob, PreventVanillaAttackHolder.KEY, PreventVanillaAttackHolder::new);
        if(data == null) return;
        if(data instanceof PreventVanillaAttackHolder v) v.time = System.currentTimeMillis();
    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Mob mob)) return;
        var type = event.getDamageSource().getDamageType();
        if(type != DamageType.MOB_ATTACK && type != DamageType.MOB_ATTACK_NO_AGGRO && type != DamageType.PLAYER_ATTACK) return;
        if(!CruxPersist.DISABLE_VANILLA_ATTACK.get(mob, false)) return;
        if(EntityMemory.getDataHolder(mob.getUniqueId(), PreventVanillaAttackHolder.KEY) instanceof PreventVanillaAttackHolder data){
            event.setCancelled(true);
            data.time += 50 * 10;
        }
    }

}
