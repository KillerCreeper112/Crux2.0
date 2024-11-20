package killercreepr.crux.api.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxEntityDamageEvent extends EntityEvent implements Cancellable {
    private final static HandlerList handlers = new HandlerList();
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

    public CruxEntityDamageEvent(@NotNull Entity victim, @Nullable Entity damager, @Nullable Location attackLoc,
                                 double trueDmg, double trueKb, double trueUpKb,
                                 double dmg, double kb, double upKb) {
        super(victim);
        this.damager = damager;
        this.trueDmg = trueDmg;
        this.trueKb = trueKb;
        this.trueUpKb = trueUpKb;
        this.dmg = dmg;
        this.kb = kb;
        this.upKb = upKb;
        this.attackLoc = attackLoc;
        if(damager != null) hitPosition = damager.getLocation();
    }

    public CruxEntityDamageEvent(@NotNull Entity victim, @Nullable Entity damager, @Nullable Location attackLoc,
                                 double trueDmg, double trueKb, double trueUpKb,
                                 double dmg, double kb, double upKb, @Nullable Location hitPosition) {
        this(victim, damager, attackLoc, trueDmg, trueKb, trueUpKb, dmg, kb, upKb);
        this.hitPosition = hitPosition;
    }

    public @Nullable DamageCause getCause() {
        return cause;
    }

    public CruxEntityDamageEvent setCause(@Nullable DamageCause cause) {
        this.cause = cause; return this;
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

    public enum DamageCause {

        /**
         * Damage caused by /kill command
         * <p>
         * Damage: {@link Float#MAX_VALUE}
         */
        KILL,
        /**
         * Damage caused by the World Border
         * <p>
         * Damage: {@link WorldBorder#getDamageAmount()}
         */
        WORLD_BORDER,
        /**
         * Damage caused when an entity contacts a block such as a Cactus,
         * Dripstone (Stalagmite) or Berry Bush.
         * <p>
         * Damage: variable
         */
        CONTACT,
        /**
         * Damage caused when an entity attacks another entity.
         * <p>
         * Damage: variable
         */
        ENTITY_ATTACK,
        /**
         * Damage caused when an entity attacks another entity in a sweep attack.
         * <p>
         * Damage: variable
         */
        ENTITY_SWEEP_ATTACK,
        /**
         * Damage caused when attacked by a projectile.
         * <p>
         * Damage: variable
         */
        PROJECTILE,
        /**
         * Damage caused by being put in a block
         * <p>
         * Damage: 1
         */
        SUFFOCATION,
        /**
         * Damage caused when an entity falls a distance greater than 3 blocks
         * <p>
         * Damage: fall height - 3.0
         */
        FALL,
        /**
         * Damage caused by direct exposure to fire
         * <p>
         * Damage: 1
         */
        FIRE,
        /**
         * Damage caused due to burns caused by fire
         * <p>
         * Damage: 1
         */
        FIRE_TICK,
        /**
         * Damage caused due to a snowman melting
         * <p>
         * Damage: 1
         */
        MELTING,
        /**
         * Damage caused by direct exposure to lava
         * <p>
         * Damage: 4
         */
        LAVA,
        /**
         * Damage caused by running out of air while in water
         * <p>
         * Damage: 2
         */
        DROWNING,
        /**
         * Damage caused by being in the area when a block explodes.
         * <p>
         * Damage: variable
         */
        BLOCK_EXPLOSION,
        /**
         * Damage caused by being in the area when an entity, such as a
         * Creeper, explodes.
         * <p>
         * Damage: variable
         */
        ENTITY_EXPLOSION,
        /**
         * Damage caused by falling into the void
         * <p>
         * Damage: 4 for players
         */
        VOID,
        /**
         * Damage caused by being struck by lightning
         * <p>
         * Damage: 5
         */
        LIGHTNING,
        /**
         * Damage caused by committing suicide.
         * <p>
         * <b>Note:</b> This is currently only used by plugins, default commands
         * like /minecraft:kill use {@link #KILL} to damage players.
         * <p>
         * Damage: variable
         */
        SUICIDE,
        /**
         * Damage caused by starving due to having an empty hunger bar
         * <p>
         * Damage: 1
         */
        STARVATION,
        /**
         * Damage caused due to an ongoing poison effect
         * <p>
         * Damage: 1
         */
        POISON,
        /**
         * Damage caused by being hit by a damage potion or spell
         * <p>
         * Damage: variable
         */
        MAGIC,
        /**
         * Damage caused by Wither potion effect
         */
        WITHER,
        /**
         * Damage caused by being hit by a falling block which deals damage
         * <p>
         * <b>Note:</b> Not every block deals damage
         * <p>
         * Damage: variable
         */
        FALLING_BLOCK,
        /**
         * Damage caused in retaliation to another attack by the Thorns
         * enchantment.
         * <p>
         * Damage: 1-4 (Thorns)
         */
        THORNS,
        /**
         * Damage caused by a dragon breathing fire.
         * <p>
         * Damage: variable
         */
        DRAGON_BREATH,
        /**
         * Custom damage.
         * <p>
         * Damage: variable
         */
        CUSTOM,
        /**
         * Damage caused when an entity runs into a wall.
         * <p>
         * Damage: variable
         */
        FLY_INTO_WALL,
        /**
         * Damage caused when an entity steps on {@link Material#MAGMA_BLOCK}.
         * <p>
         * Damage: 1
         */
        HOT_FLOOR,
        /**
         * Damage caused when an entity is colliding with too many entities due
         * to the maxEntityCramming game rule.
         * <p>
         * Damage: 6
         */
        CRAMMING,
        /**
         * Damage caused when an entity that should be in water is not.
         * <p>
         * Damage: 1
         */
        DRYOUT,
        /**
         * Damage caused from freezing.
         * <p>
         * Damage: 1 or 5
         */
        FREEZE,
        /**
         * Damage caused by the Sonic Boom attack from {@link org.bukkit.entity.Warden}
         * <p>
         * Damage: 10
         */
        SONIC_BOOM;

        public @Nullable EntityDamageEvent.DamageCause bukkit(){
            try{ return EntityDamageEvent.DamageCause.valueOf(this.toString()); }
            catch (IllegalArgumentException e){ return null; }
        }

        public static @Nullable DamageCause grim(@Nullable EntityDamageEvent.DamageCause convert){
            if(convert == null) return null;
            try{ return DamageCause.valueOf(convert.toString()); }
            catch (IllegalArgumentException e){ return null; }
        }
    }
}
