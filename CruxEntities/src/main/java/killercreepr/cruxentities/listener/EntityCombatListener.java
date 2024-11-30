package killercreepr.cruxentities.listener;

import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxAttributeInstance;
import killercreepr.cruxentities.api.combat.EntityDamager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class EntityCombatListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile proj = event.getEntity();
        Entity hit = event.getHitEntity();
        if(hit == null) return;
        CruxAttributeInstance dmgInstance = CruxAttribute.getInstance(proj, CruxAttribute.ATTACK_DAMAGE);
        CruxAttributeInstance kbInstance = CruxAttribute.getInstance(proj, CruxAttribute.ATTACK_KNOCKBACK);
        CruxAttributeInstance kbUpInstance = CruxAttribute.getInstance(proj, CruxAttribute.ATTACK_KNOCKBACK_UP);
        if(dmgInstance == null && kbInstance == null && kbUpInstance == null) return;

        EntityDamager.entityDamager(hit, proj).attack();
    }

}
