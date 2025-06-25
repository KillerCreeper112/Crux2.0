package killercreepr.cruxentities.listener;

import org.bukkit.event.Listener;

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

}
