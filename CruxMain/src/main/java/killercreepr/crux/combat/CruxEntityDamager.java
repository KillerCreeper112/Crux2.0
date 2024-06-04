package killercreepr.crux.combat;

import killercreepr.crux.Crux;
import killercreepr.crux.attribute.CruxAttribute;
import killercreepr.crux.event.CruxEntityDamageEvent;
import killercreepr.crux.event.CruxEntityDeathEvent;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxEntityDamager {
    private Entity damager;
    private Entity target;
    private Location hitPosition;
    private CruxEntityDamageEvent.DamageCause cause;

    public CruxEntityDamager(@NotNull Entity target) {
        this.target = target;
    }

    public CruxEntityDamager(@NotNull Entity target, @Nullable Entity damager) {
        this.damager = damager;
        this.target = target;
        if(damager != null) hitPosition = damager.getLocation();
    }

    public @Nullable Location getHitPosition() {
        return hitPosition;
    }

    public CruxEntityDamager setHitPosition(@Nullable Location hitPosition) {
        this.hitPosition = hitPosition; return this;
    }

    public @Nullable Entity getDamager() {
        return damager;
    }

    public @NotNull Entity getTarget() {
        return target;
    }

    public CruxEntityDamager setDamager(@Nullable Entity damager) {
        this.damager = damager;
        return this;
    }

    public CruxEntityDamager setTarget(@NotNull Entity target) {
        this.target = target;
        return this;
    }
    /**
     * @return A new CruxEntityDamager with the new target value.
     */
    public @NotNull CruxEntityDamager withTarget(@NotNull Entity target){
        return new CruxEntityDamager(target, damager);
    }

    /**
     * @return A new CruxEntityDamager with the new damager value.
     */
    public @NotNull CruxEntityDamager withDamager(@Nullable Entity damager){
        return new CruxEntityDamager(target, damager);
    }

    public @NotNull Vector applyKnockback(double kb, double upKb, @NotNull Location attackLoc){
        return applyKnockback(target, attackLoc, kb, upKb);
    }

    public @NotNull Vector applyKnockback(double kb, double upKb){
        if(damager == null) return new Vector();
        return applyKnockback(kb, upKb, damager.getLocation());
    }

    public @NotNull Vector applyKnockback(double kb){
        return applyKnockback(kb, 0D);
    }

    public @NotNull Vector applyKnockback(@NotNull Entity target, @NotNull Location attackLoc, double kb){
        return applyKnockback(target, attackLoc, kb, 0f);
    }

    public @NotNull Vector applyKnockback(@NotNull Entity target, @NotNull Location attackLoc, double kb, double upKb){
        return applyKnockback(target, attackLoc, kb, upKb, true);
    }

    public @NotNull Vector applyKnockback(@NotNull Entity target, @NotNull Location attackLoc, double kb, double upKb, boolean add){
        Vector delta = calculateEntityVelocity(attackLoc, target, kb, upKb);
        if(add) delta = target.getVelocity().add(delta);
        try{ target.setVelocity(delta); }
        catch (IllegalArgumentException ignored){}
        return delta;
    }

    public static @NotNull Vector calculateEntityVelocity(@NotNull Location attackerLoc, @NotNull Entity victim, double kb, double upKb){
        kb = kb/20D;
        upKb = upKb/20D;
        double x = attackerLoc.getX()- victim.getX();
        double z = attackerLoc.getZ() - victim.getZ();

        Vector v = victim.getVelocity();
        Vector pos = new Vector(x, 0D, z).normalize().multiply(kb);
        Vector currentMovement = new Vector(
                v.getX() / 2D - pos.getX(),
                victim.isOnGround() ? Math.min(0.4D, v.getY() / 2D + kb) : v.getY(),
                v.getZ() / 2D - pos.getZ()
        );
        if(upKb != 0D) currentMovement.setY(currentMovement.getY()+upKb);
        return new Vector(currentMovement.getX() - v.getX(), currentMovement.getY() - v.getY(), currentMovement.getZ() - v.getZ());
    }

    public static @NotNull Vector calculateEntityVelocity(@NotNull Entity attacker, @NotNull Entity victim, double kb, double upKb){
        return calculateEntityVelocity(attacker.getLocation(), victim, kb, upKb);
    }

    public double calculateKnockback(double trueKb){
        return trueKb * Math.max((1D-(getKnockbackResistance(target) * .05D)), 0D);
    }

    public static double getKnockbackResistance(@NotNull Entity e){
        double x = CruxAttribute.get(e, CruxAttribute.KNOCKBACK_RESISTANCE);
        if(e instanceof LivingEntity d){
            AttributeInstance i = d.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
            if(i != null) x+=i.getValue();
        }
        return x;
    }

    public double calculateUpKnockback(double trueUpKb){
        return calculateKnockback(trueUpKb);
    }

    public double calculateDamage(double trueDamage){
        double dmg = trueDamage;
        double armor = CruxAttribute.get(target, CruxAttribute.ARMOR);
        double toughness = CruxAttribute.get(target, CruxAttribute.ARMOR_TOUGHNESS);
        if(target instanceof LivingEntity e){
            AttributeInstance i = e.getAttribute(Attribute.GENERIC_ARMOR);
            if(i != null) armor += i.getValue();
            i = e.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);
            if(i != null) toughness += i.getValue();
        }

        double var =  armor - (dmg / (2f + (toughness / 4f)));
        double def5 = armor / 5f;

        double var2 = Math.max(var, def5);
        double var3 = Math.min(20D, var2);

        dmg *= (1f - (var3 / 25f));
        if(target instanceof LivingEntity e){
            PotionEffect resistance = e.getPotionEffect(PotionEffectType.RESISTANCE);
            if(resistance != null){
                dmg *= Math.max(1D - (.1D * (resistance.getAmplifier() + 1)), 0D);
            }
        }
        return dmg;
    }

    public @Nullable CruxEntityDamageEvent attack(double damage, double kb, double upkb,
                                                  @NotNull Entity target,
                                                  @Nullable Entity damager, @Nullable Location attackLoc){
        if(target instanceof LivingEntity e && e.getNoDamageTicks() > 0) return null;
        CruxEntityDamageEvent event = new CruxEntityDamageEvent(target, damager, attackLoc,
                damage, kb, upkb,
                calculateDamage(damage), calculateKnockback(kb), calculateUpKnockback(upkb))
                .setCause(cause);
        if(!event.callEvent()) return event;
        if(event.getEntity() instanceof LivingEntity e){
            //death
            if(e.getHealth() - event.getDmg() <= 0D){
                CruxEntityDeathEvent deathEvent = new CruxEntityDeathEvent(event);
                if(!deathEvent.callEvent()){
                    event.setCancelled(true);
                    return event;
                }
            }
            e.damage(event.getDmg());
            e.setNoDamageTicks(0);
        }
        if(event.getAttackLoc() == null || (event.getKb() == 0D && event.getUpKb() == 0D)) return event;
        new BukkitRunnable(){
            @Override
            public void run() {
                applyKnockback(event.getEntity(), event.getAttackLoc(), event.getKb(), event.getUpKb());
            }
        }.runTaskLater(Crux.getMainPlugin(), 1L);
        return event;
    }

    public @Nullable CruxEntityDamageEvent attack(double damage, double kb, double upkb, @Nullable Location attackLoc){
        return attack(damage, kb, upkb, target, damager, attackLoc);
    }

    public @Nullable CruxEntityDamageEvent attack(double damage, double kb, double upkb){
        return attack(damage, kb, upkb, target, damager, damager == null ? null : damager.getLocation());
    }

    public @Nullable CruxEntityDamageEvent attack(double damage, double kb){
        return attack(damage, kb, 0D);
    }

    public @Nullable CruxEntityDamageEvent attack(double damage){
        return attack(damage, 0D, 0D);
    }

    /**
     * Attacks the victim using the default attack damage, and knockback attributes.
     */
    public @Nullable CruxEntityDamageEvent attack(){
        return attack(getDamage(damager),
                CruxAttribute.get(damager, CruxAttribute.ATTACK_KNOCKBACK),
                CruxAttribute.get(damager, CruxAttribute.ATTACK_KNOCKBACK_UP));
    }

    public @Nullable CruxEntityDamageEvent attackWithMultiplier(double x){
        return attack(getDamage(damager)*x,
                CruxAttribute.get(damager, CruxAttribute.ATTACK_KNOCKBACK)*x,
                CruxAttribute.get(damager, CruxAttribute.ATTACK_KNOCKBACK_UP)*x);
    }

    public static double getDamage(@Nullable Entity dmger){
        if(dmger instanceof LivingEntity d && d.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null){
            return d.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue() + CruxAttribute.get(dmger, CruxAttribute.ATTACK_DAMAGE);
        }
        return CruxAttribute.get(dmger, CruxAttribute.ATTACK_DAMAGE);
    }

    public @Nullable CruxEntityDamageEvent.DamageCause getCause() {
        return cause;
    }

    public CruxEntityDamager setCause(@Nullable CruxEntityDamageEvent.DamageCause cause) {
        this.cause = cause; return this;
    }
}
