package killercreepr.crux.api.event;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.crux.core.Crux;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxEntityDamageEvent extends Event implements Cancellable {
    private final static HandlerList handlers = new HandlerList();
    protected final Entity victim;
    protected Entity damager;
    protected double dmg;
    protected double kb;
    protected double upKb;
    protected Location attackLoc;
    protected Location hitPosition;

    protected final double trueDmg;
    protected final double trueKb;
    protected final double trueUpKb;
    protected DamageCause cause;
    protected final DamageSource trueSource;
    protected DamageSource source;

    public CruxEntityDamageEvent(@NotNull Entity victim, @Nullable Entity damager, @Nullable Location attackLoc,
                                 double trueDmg, double trueKb, double trueUpKb,
                                 double dmg, double kb, double upKb, DamageSource trueSource) {
        super(!Crux.isPrimaryThread());
        this.victim = victim;
        this.damager = damager;
        this.trueDmg = trueDmg;
        this.trueKb = trueKb;
        this.trueUpKb = trueUpKb;
        this.dmg = dmg;
        this.kb = kb;
        this.upKb = upKb;
        this.attackLoc = attackLoc;
        this.trueSource = trueSource;
        if(damager != null) hitPosition = damager.getLocation();
    }

    public CruxEntityDamageEvent(@NotNull Entity victim, @Nullable Entity damager, @Nullable Location attackLoc,
                                 double trueDmg, double trueKb, double trueUpKb,
                                 double dmg, double kb, double upKb, @Nullable Location hitPosition, DamageSource trueSource) {
        this(victim, damager, attackLoc, trueDmg, trueKb, trueUpKb, dmg, kb, upKb, trueSource);
        this.hitPosition = hitPosition;
    }

    public DamageSource getTrueSource() {
        return trueSource;
    }

    public @NotNull Entity getEntity() {
        return victim;
    }

    public @NotNull Entity getVictim() {
        return victim;
    }

    public @Nullable DamageCause getCause() {
        return cause;
    }

    public CruxEntityDamageEvent setCause(@Nullable DamageCause cause) {
        this.cause = cause; return this;
    }

    /**
     * Could mess up things if you mess with this
     */
    @ApiStatus.Experimental
    public void setSource(DamageSource source) {
        this.source = source;
    }

    public DamageSource getSource() {
        return source;
    }

    public @Nullable Location getHitPosition() {
        return hitPosition;
    }

    public void setHitPosition(@Nullable Location hitPosition) {
        this.hitPosition = hitPosition;
    }

    public Location getAttackLoc() {
        return attackLoc;
    }

    public void setAttackLoc(Location attackLoc) {
        this.attackLoc = attackLoc;
    }

    public @Nullable Entity getDamager() {
        return damager;
    }

    public void setDamager(@Nullable Entity damager) {
        this.damager = damager;
    }

    public double getDmg() {
        return dmg;
    }

    public void setDmg(double dmg) {
        this.dmg = dmg;
    }

    public double getKb() {
        return kb;
    }

    public void setKb(double kb) {
        this.kb = kb;
    }

    public double getUpKb() {
        return upKb;
    }

    public void setUpKb(double upKb) {
        this.upKb = upKb;
    }

    public double getTrueDmg() {
        return trueDmg;
    }

    public double getTrueKb() {
        return trueKb;
    }

    public double getTrueUpKb() {
        return trueUpKb;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NotNull HandlerList getHandlerList(){ return handlers; }

    private boolean cancel = false;
    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public interface DamageCause extends CruxKeyed {
        static DamageCause fromBukkit(EntityDamageEvent.DamageCause cause){
            return new BukkitCause(cause);
        }

        static EntityDamageEvent.DamageCause toBukkit(DamageCause cause){
            if(!cause.key().namespace().equalsIgnoreCase(Key.MINECRAFT_NAMESPACE)) return null;
            try{
                return EntityDamageEvent.DamageCause.valueOf(cause.key().value().toUpperCase());
            }catch (IllegalArgumentException ignored){
                return null;
            }
        }

        class BukkitCause implements DamageCause{
            public final EntityDamageEvent.DamageCause cause;

            public BukkitCause(EntityDamageEvent.DamageCause cause) {
                this.cause = cause;
            }

            @Override
            public @NotNull Key key() {
                return Key.key(cause.toString().toLowerCase());
            }
        }
    }
}
