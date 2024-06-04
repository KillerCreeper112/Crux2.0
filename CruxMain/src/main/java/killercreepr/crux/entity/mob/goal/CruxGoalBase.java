package killercreepr.crux.entity.mob.goal;

import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import killercreepr.crux.Crux;
import killercreepr.crux.attribute.CruxAttribute;
import killercreepr.crux.combat.CruxEntityDamager;
import killercreepr.crux.combat.EntityHit;
import killercreepr.crux.event.CruxEntityDamageEvent;
import killercreepr.crux.persistence.PersistTag;
import killercreepr.crux.util.CruxEntity;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class CruxGoalBase {
    public final static @NotNull Predicate<Entity> UNDESIRED_BEHAVIOR = e ->{
        if(e.getType() == EntityType.UNKNOWN) return false;
        if(e instanceof Player p){
            return (p.getGameMode() != GameMode.CREATIVE && p.getGameMode() != GameMode.SPECTATOR) && p.isValid();
        }
        return e instanceof LivingEntity && e.getType() != EntityType.ARMOR_STAND && !PersistTag.IGNORED_MOB_TARGET.has(e) && e.isValid();
    };

    protected final GoalKey<Mob> key;
    protected final Mob mob;
    protected LivingEntity target;
    protected Location lastKnownTargetLocation;
    protected int attackCooldown;

    public CruxGoalBase setAttackCooldown(int attackCooldown) {
        this.attackCooldown = attackCooldown;
        return this;
    }

    protected int findCooldown;
    protected int lostTarget;

    public static @NotNull GoalKey<Mob> defaultKey(){ return GoalKey.of(Mob.class, Crux.key("crux_goal")); }

    public CruxGoalBase(@NotNull Mob mob){
        this(defaultKey(), mob);
    }

    public CruxGoalBase(@NotNull GoalKey<Mob> key, @NotNull Mob mob){
        this.key = key;
        this.mob = mob;
    }


    public @NotNull GoalKey<Mob> getKey() {
        return key;
    }
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.UNKNOWN_BEHAVIOR);
    }

    public @Nullable LivingEntity getTarget(){ return target; }
    public final @NotNull Mob getMob(){ return mob; }

    public void setTarget(@Nullable LivingEntity newTarget){ this.target = newTarget; }

    protected boolean updateTarget(){ return target == null || !isValidTarget(target); }

    public boolean isValidAttackerTarget(@NotNull LivingEntity target){
        return isValidTarget(target);
    }

    public boolean isValidNaturalTarget(@NotNull LivingEntity target){
        return isValidTarget(target) && (hasLineOfSight(target));
    }


    protected boolean isValidTarget(@NotNull LivingEntity target){
        if(!mob.getWorld().equals(target.getWorld()) ||
                mob.getLocation().distanceSquared(target.getLocation()) > (getForgetTargetDistance()*getForgetTargetDistance()) /*||
                TeamUtility.areMatchingOrAlly(mob, target)*/) return false;
        return UNDESIRED_BEHAVIOR.test(target);
    }

    protected double getForgetTargetDistance(){
        return 128D;
    }

    protected double getFollowDistance(){
        AttributeInstance i = mob.getAttribute(Attribute.GENERIC_FOLLOW_RANGE);
        return i == null ? 16D : i.getValue();
    }

    protected int maxLostTargetTicks(){
        return 300;
    }

    protected int searchLostTargetTicks(){
        return 200;
    }

    protected int followLostTargetTicks(){
        return 100;
    }

    public void forgetTarget(){
        setTarget(null);
        lastKnownTargetLocation = null;
        lostTarget = 0;
    }

    public boolean knowsWhereTargetIs(){
        return lastKnownTargetLocation == null && target != null;
    }

    protected void moveTo(){
        if(target != null){
            if(lastKnownTargetLocation == null || lostTarget < followLostTargetTicks()){
                mob.getPathfinder().moveTo(target);
            }else{
                if(lostTarget < searchLostTargetTicks()) mob.getPathfinder().moveTo(target);
                else if(lostTarget >= searchLostTargetTicks() && lostTarget <= searchLostTargetTicks()+5) mob.getPathfinder().moveTo(mob.getLocation());
            }
        }
    }

    protected boolean hasLineOfSight(@NotNull LivingEntity target){
        if(!target.getWorld().equals(mob.getWorld())) return false;
        if(target.hasPotionEffect(PotionEffectType.GLOWING)) return true;
        if(!mob.hasLineOfSight(target)) return false;
        return !target.hasPotionEffect(PotionEffectType.INVISIBILITY) || target.getLocation().distanceSquared(mob.getLocation()) < (4*4);
    }

    protected void targetLogic(){
        if(target == null) return;
        if(hasLineOfSight(target)){
            if(lastKnownTargetLocation != null){
                lastKnownTargetLocation = null;
                lostTarget = 0;
            }
        }else{
            if(lastKnownTargetLocation == null){
                lastKnownTargetLocation = target.getLocation();
                lostTarget = 0;
            }
            if(lostTarget++ > maxLostTargetTicks()) forgetTarget();
        }
    }

    protected boolean findTarget(@Nullable Predicate<Entity> targetCheck){
        double followRange = getFollowDistance();

        final List<LivingEntity> targets = CruxEntity.filterEntityDistance(CruxEntity.getEntitiesNear(
            LivingEntity.class,
            mob.getLocation(),
            followRange,
            e -> isValidNaturalTarget(e) && (targetCheck == null || targetCheck.test(e))
        ), mob.getLocation(), -1, false);

        /*todo List<Mob> teammates = new GetNearbyEntities(mob, followRange).get(Mob.class, null, TeamType.TEAMMATE);
        for(LivingEntity e : targets){
            for(Mob m : teammates){
                if(Bukkit.getMobGoals().getGoal(m, getKey()) instanceof CruxGoalBase path && e.equals(path.getTarget())) continue;
                setTarget(e);
                return true;
            }
        }*/
        if(!targets.isEmpty()){
            setTarget(targets.getFirst());
            return true;
        }
        return false;
    }

    private @Nullable EntityHit.Result hit(){
        return hit(false, (Entity[]) null);
    }

    protected @Nullable EntityHit.Result hit(boolean lookAt, @Nullable Entity @Nullable... targets){
        if(!preAttemptAttack()) return null;
        float attackCooldown = 1f; //Could be used for something down the road.

        double range = CruxAttribute.ATTACK_RANGE.get(mob) * Math.max(attackCooldown, .25f);
        double aoe = CruxAttribute.ATTACK_AOE.get(mob) * Math.max(attackCooldown, .25f);
        int pierce = (int) (CruxAttribute.ATTACK_PIERCE.get(mob) * Math.max(attackCooldown, .25f)) + 1;

        final Vector direction;
        if(lookAt && targets != null && targets.length > 0 && targets[0] != null){
            if(targets[0] instanceof LivingEntity lE){
                direction = lE.getEyeLocation().toVector().subtract(mob.getEyeLocation().toVector());
            }else direction = targets[0].getLocation().toVector().subtract(mob.getEyeLocation().toVector());
        }else direction = mob.getEyeLocation().getDirection();
        EntityHit.Result result = new EntityHit(mob.getEyeLocation(), direction.length() <= 0D ? mob.getEyeLocation().getDirection() : direction, range, aoe, pierce)
                .getHitEntities(e ->{
                    if(targets != null && targets.length > 0 && (targets.length > 1 || targets[0] != null)) return List.of(targets).contains(e);
                    return !e.equals(mob); /*&& !TeamUtility.areMatchingOrAlly(mob, e);*/
                });
        Entity e = mob.getTargetEntity(1);
        if(e != null && !result.getHit().contains(e)){
            result.getHit().add(e);
            result.getResults().add(new RayTraceResult(mob.getEyeLocation().toVector(), e, null));
        }
        if(result.getHit().isEmpty()) return result;
        double trueDmg = (
            (mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) == null ? 0D : mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue())
                + CruxAttribute.ATTACK_DAMAGE.get(mob)
        ) * Math.max(attackCooldown, .75f);
        double trueKb = CruxAttribute.ATTACK_KNOCKBACK.get(mob) * Math.max(attackCooldown, .75f);
        double trueUpKb = CruxAttribute.ATTACK_KNOCKBACK_UP.get(mob) * Math.max(attackCooldown, .75f);

        double dmg;
        double dmgDropOff = 1D;
        double kbDropOff = 1D;
        for(RayTraceResult r : result.getResults()){
            if(r.getHitEntity() != null){
                dmg = trueDmg;
                CruxEntityDamageEvent event = new CruxEntityDamager(r.getHitEntity(), mob).attack(dmg, trueKb, trueUpKb);
                if(event != null) attacked(event);
                if(event == null || event.isCancelled()) continue;
                dmgDropOff = Math.max(dmgDropOff - .1f, .1f);
                kbDropOff = Math.max(kbDropOff - .1f, .1f);
            }
        }
        return result;
    }

    /**
     * @return Whether the mob should stick with the attack attempt.
     */
    public boolean preAttemptAttack(){
        return true;
    }

    protected void attacked(@NotNull CruxEntityDamageEvent event){}

    protected @Nullable EntityHit.Result attemptAttack(){
        EntityHit.Result result = hit(true);
        if(result != null) attackCooldown = (int) Math.ceil(CruxAttribute.ATTACK_SPEED.get(mob));
        return result;
    }

    protected @Nullable EntityHit.Result attemptAttack(@Nullable LivingEntity target, double distance){
        EntityHit.Result result = null;
        if(distance <= CruxAttribute.ATTACK_RANGE.get(mob) && attackCooldown <= 0){
            result = hit(true, target);
            if(result != null) attackCooldown = (int) Math.ceil(CruxAttribute.ATTACK_SPEED.get(mob));
        }
        if(attackCooldown > 0) attackCooldown--;
        return result;
    }

    public boolean canAttack(){
        return attackCooldown < 1;
    }
}
