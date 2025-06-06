package killercreepr.crux.api.event;

import org.bukkit.Location;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxEntityDeathEvent extends CruxEntityDamageEvent {
    private final static HandlerList handlers = new HandlerList();

    public CruxEntityDeathEvent(@NotNull Entity victim, @Nullable Entity damager, @Nullable Location attackLoc, double trueDmg, double trueKb, double trueUpKb, double dmg, double kb, double upKb, DamageSource trueSource) {
        super(victim, damager, attackLoc, trueDmg, trueKb, trueUpKb, dmg, kb, upKb, trueSource);
    }

    public CruxEntityDeathEvent(@NotNull Entity victim, @Nullable Entity damager, @Nullable Location attackLoc, double trueDmg, double trueKb, double trueUpKb, double dmg, double kb, double upKb, @Nullable Location hitPosition, DamageSource trueSource) {
        super(victim, damager, attackLoc, trueDmg, trueKb, trueUpKb, dmg, kb, upKb, hitPosition, trueSource);
    }

    public CruxEntityDeathEvent(CruxEntityDamageEvent event){
        super(event.getEntity(), event.getDamager(), event.getAttackLoc(), event.getTrueDmg(), event.getTrueKb(), event.getTrueUpKb(), event.getDmg(),
            event.getKb(), event.getUpKb(), event.getHitPosition(), event.getTrueSource());
        setCancelled(event.isCancelled());
        setCause(event.getCause());
        setSource(event.getSource());
    }


    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NotNull HandlerList getHandlerList(){ return handlers; }
}
