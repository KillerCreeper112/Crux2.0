package killercreepr.cruxentities.api.combat;

import killercreepr.crux.api.event.CruxEntityDamageEvent;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.persistence.CruxPersist;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxAttributeAccessor;
import killercreepr.cruxentities.combat.CruxEntityDamager;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface EntityDamager {
    static EntityDamager entityDamager(@NotNull Entity target){
        return new CruxEntityDamager(target);
    }
    static EntityDamager entityDamager(@NotNull Entity target, @Nullable Entity damager){
        return new CruxEntityDamager(target, damager);
    }

    static @NotNull Entity getOwnerOrSelf(@NotNull Entity base){
        Entity owner = getOwner(base);
        return owner == null ? base : owner;
    }

    static @Nullable Entity getOwner(@NotNull Entity base){
        UUID ownerUUID = CruxPersist.OWNER.get(base);
        if(ownerUUID != null){
            return Crux.getServer().getEntity(ownerUUID);
        }
        if(base instanceof Projectile p){
            if(p.getShooter() instanceof Entity e) return e;
        }
        if(base instanceof Item i){
            ownerUUID = i.getOwner();
            if(ownerUUID != null){
                var e = Crux.getServer().getEntity(ownerUUID);
                if(e != null) return e;
            }

            ownerUUID = i.getThrower();
            if(ownerUUID != null){
                var e = Crux.getServer().getEntity(ownerUUID);
                if(e != null) return e;
            }
        }
        return base;
    }

    static @Nullable UUID getOwnerUUID(@NotNull Entity base){
        UUID ownerUUID = CruxPersist.OWNER.get(base);
        if(ownerUUID != null){
            return ownerUUID;
        }
        if(base instanceof Projectile p){
            if(p.getShooter() instanceof Entity e) return e.getUniqueId();
        }
        if(base instanceof Item i){
            ownerUUID = i.getOwner();
            if(ownerUUID != null){
                return ownerUUID;
            }

            ownerUUID = i.getThrower();
            if(ownerUUID != null){
                return ownerUUID;
            }
        }
        return base.getUniqueId();
    }

    static @Nullable Entity getShooter(@NotNull Entity base){
        UUID ownerUUID = CruxPersist.OWNER.get(base);
        if(ownerUUID != null){
            return Crux.getServer().getEntity(ownerUUID);
        }

        if(base instanceof Projectile p){
            if(p.getShooter() instanceof Entity e) return e;
            return null;
        }
        return null;
    }

    static @NotNull Vector calculateEntityVelocity(@NotNull Location attackerLoc, @NotNull Entity victim, double kb, double upKb){
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

    static @NotNull Vector calculateEntityVelocity(@NotNull Entity attacker, @NotNull Entity victim, double kb, double upKb){
        return calculateEntityVelocity(attacker.getLocation(), victim, kb, upKb);
    }

    static double getKnockbackResistance(@NotNull Entity e){
        double x = CruxAttribute.get(e, CruxAttribute.KNOCKBACK_RESISTANCE);
        if(e instanceof LivingEntity d){
            AttributeInstance i = d.getAttribute(Attribute.KNOCKBACK_RESISTANCE);
            if(i != null) x+=i.getValue();
        }
        return x;
    }

    static double getKnockback(@Nullable Entity dmger){
        if(dmger == null) return 0D;
        if(CruxAttribute.hasAttributeData(dmger, CruxAttribute.ATTACK_KNOCKBACK)){
            return CruxAttribute.get(dmger, CruxAttribute.ATTACK_KNOCKBACK);
        }

        if(dmger instanceof ThrowableProjectile) return 10D;
        return 0D;
    }

    static double getDamage(@Nullable Entity dmger){
        if(dmger instanceof LivingEntity d && d.getAttribute(Attribute.ATTACK_DAMAGE) != null){
            return d.getAttribute(Attribute.ATTACK_DAMAGE).getValue() + CruxAttribute.get(dmger, CruxAttribute.ATTACK_DAMAGE);
        }
        var instance = CruxAttribute.getInstance(dmger, CruxAttribute.ATTACK_DAMAGE);
        if(instance != null) return instance.getValue();

        if(dmger instanceof AbstractArrow d){
            double damage = d.getDamage();
            double f = d.getVelocity().length();
            damage = Math.ceil(Math.clamp(f * damage, 0f, Integer.MAX_VALUE));
            if(d.isCritical()){
                long l = CruxMath.random().nextLong((long)damage / 2L + 2L);
                damage = (int)Math.min(l + (long)damage, 2147483647L);
            }
            return damage;
        }
        return 0D;
    }
    @Nullable
    CruxEntityDamageEvent attack(CruxAttributeAccessor attributes, boolean applySpecial);
    EntityDamager applySpecialAttacks(CruxAttributeAccessor attributes);

    EntityDamager setCalculateCustomDamage();

    @Nullable
    Location getHitPosition();

    EntityDamager setHitPosition(@Nullable Location hitPosition);

    @Nullable
    Entity getDamager();

    @NotNull
    Entity getTarget();

    EntityDamager setDamager(@Nullable Entity damager);

    EntityDamager setTarget(@NotNull Entity target);

    /**
     * @return A new CruxEntityDamager with the new target value.
     */
    @NotNull
    EntityDamager withTarget(@NotNull Entity target);
    /**
     * @return A new CruxEntityDamager with the new damager value.
     */
    @NotNull
    EntityDamager withDamager(@Nullable Entity damager);

    @NotNull
    Vector applyKnockback(double kb, double upKb, @NotNull Location attackLoc);

    @NotNull
    Vector applyKnockback(double kb, double upKb);

    @NotNull
    Vector applyKnockback(double kb);

    @NotNull
    Vector applyKnockback(@NotNull Entity target, @NotNull Location attackLoc, double kb);

    @NotNull
    Vector applyKnockback(@NotNull Entity target, @NotNull Location attackLoc, double kb, double upKb);

    @NotNull
    Vector applyKnockback(@NotNull Entity target, @NotNull Location attackLoc, double kb, double upKb, boolean add);
    double calculateKnockback(double trueKb);

    double calculateUpKnockback(double trueUpKb);

    double calculateDamage(double trueDamage);
    double calculateDamage(double trueDamage, double armorPenetration);

    boolean isDamageSourceBlocked();

    @Nullable
    CruxEntityDamageEvent attack(double damage, double kb, double upkb, @NotNull Entity target, @Nullable Entity damager, @Nullable Location attackLoc);

    @Nullable
    CruxEntityDamageEvent attack(double damage, double kb, double upkb, @Nullable Location attackLoc);

    @Nullable
    CruxEntityDamageEvent attack(double damage, double kb, double upkb);

    @Nullable
    CruxEntityDamageEvent attack(double damage, double kb);

    @Nullable
    CruxEntityDamageEvent attack(double damage);

    /**
     * Attacks the victim using the default attack damage, and knockback attributes.
     */
    @Nullable
    CruxEntityDamageEvent attack();

    @Nullable
    CruxEntityDamageEvent attackWithMultiplier(double x);
    @Nullable
    CruxEntityDamageEvent.DamageCause getCause();

    EntityDamager setCause(@Nullable CruxEntityDamageEvent.DamageCause cause);

    @Nullable
    DamageSource getSource();

    EntityDamager setSource(@Nullable DamageSource source);
}
