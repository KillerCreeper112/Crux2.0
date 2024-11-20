package killercreepr.crux.api.event;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxEntityDeathEvent extends CruxEntityDamageEvent {
    private final static HandlerList handlers = new HandlerList();

    public CruxEntityDeathEvent(@NotNull Entity victim, @Nullable Entity damager, @Nullable Location attackLoc, double trueDmg, double trueKb, double trueUpKb, double dmg, double kb, double upKb) {
        super(victim, damager, attackLoc, trueDmg, trueKb, trueUpKb, dmg, kb, upKb);
    }

    public CruxEntityDeathEvent(@NotNull Entity victim, @Nullable Entity damager, @Nullable Location attackLoc, double trueDmg, double trueKb, double trueUpKb, double dmg, double kb, double upKb, @Nullable Location hitPosition) {
        super(victim, damager, attackLoc, trueDmg, trueKb, trueUpKb, dmg, kb, upKb, hitPosition);
    }

    public CruxEntityDeathEvent(@NotNull CruxEntityDamageEvent event){
        this(event.getEntity(), event.getDamager(), event.getAttackLoc(), event.getTrueDmg(), event.getTrueKb(), event.getTrueUpKb(),
                event.getDmg(), event.getKb(), event.getUpKb(), event.getHitPosition());
        setCancelled(event.isCancelled());
        setCause(event.getCause());
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NotNull HandlerList getHandlerList(){ return handlers; }
}
