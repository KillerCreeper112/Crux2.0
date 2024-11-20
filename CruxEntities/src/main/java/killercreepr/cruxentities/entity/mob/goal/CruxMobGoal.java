package killercreepr.cruxentities.entity.mob.goal;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import killercreepr.crux.core.Crux;
import killercreepr.cruxentities.entity.mob.goal.sound.CruxGoalSounds;
import killercreepr.cruxentities.entity.mob.goal.sound.SoundedMob;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class CruxMobGoal extends CruxGoalBase implements Goal<Mob>, ICruxMobGoal, Listener, SoundedMob {
    protected Predicate<Entity> targetCheck;
    protected CruxGoalSounds sounds;
    public CruxMobGoal(@NotNull Mob mob) {
        super(mob);
    }
    public CruxMobGoal(@NotNull GoalKey<Mob> key, @NotNull Mob mob){
        super(key, mob);
    }

    protected boolean isValid(){ return mob.isValid(); }
    @Override
    public boolean shouldActivate() {
        return isValid();
    }

    @Override
    public boolean shouldStayActive() {
        return shouldActivate();
    }
    public @NotNull CruxMobGoal sounds(@Nullable CruxGoalSounds sounds){ this.sounds = sounds; return this; }

    @Override
    public @Nullable CruxGoalSounds getGoalSounds() {
        return sounds;
    }

    @Override
    public boolean isInAttackRange(double distance){
        return distance <= 5D;
    }

    @Override
    public void start() {
        Bukkit.getPluginManager().registerEvents(this, Crux.getMainPlugin());
    }

    @EventHandler
    public void entityDamageMob(EntityDamageByEntityEvent event) {
        if (event.getEntity().equals(this.mob)) {
            if (event.getDamager() instanceof LivingEntity damager) {
                if (!damager.getWorld().equals(this.mob.getWorld()) || !this.isValidAttackerTarget(damager)) {
                    return;
                }
                if (this.target == null) {
                    this.setTarget(damager);
                } else {
                    double targetDistance = this.mob.getLocation().distance(this.target.getLocation());
                    if (this.isInAttackRange(targetDistance)) return;

                    double damagerDistance = this.mob.getLocation().distance(damager.getLocation());
                    if(damagerDistance < targetDistance) this.setTarget(damager);
                }
            }
        }
    }

    protected boolean hasBeenRemovedOrDied = false;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {
        if(event.getEntity().equals(mob) && !hasBeenRemovedOrDied){
            hasBeenRemovedOrDied = true;
            onRemovalOrDeath(true);
            Bukkit.getMobGoals().removeGoal(mob, key);
        }
    }

    /**
     * Called when the mob dies or gets removed from the world.
     * @param died If true, the mob died. Otherwise, the mob was removed.
     */
    protected void onRemovalOrDeath(boolean died){}

    @EventHandler(priority = EventPriority.MONITOR)
    public final void entityRemove(EntityRemoveFromWorldEvent event) {
        if(event.getEntity().equals(this.mob)){
            HandlerList.unregisterAll(this);
            if(!hasBeenRemovedOrDied) onRemovalOrDeath(false);
            hasBeenRemovedOrDied = true;
            Bukkit.getMobGoals().removeGoal(mob, key);
        }
    }

    @Override
    public void stop() {
        mob.getPathfinder().stopPathfinding();
        mob.setTarget(null);
    }

    protected double getSquaredDistanceFromTarget(){
        if(target != null && target.getWorld().equals(mob.getWorld())) return mob.getLocation().distanceSquared(target.getLocation());
        return 0D;
    }

    protected double getDistanceFromTarget(){
        if(target != null && target.getWorld().equals(mob.getWorld())) return mob.getLocation().distance(target.getLocation());
        return 0D;
    }

    protected boolean isWithinSquaredDistance(double distance, double range){
        return distance <= (range*range);
    }

    protected boolean isWithinSquaredDistanceFromTarget(double range){
        return getSquaredDistanceFromTarget() <= (range*range);
    }

    @Override
    public void tick() {
        checkTargetLogic();
        targetLogic();
        sound();
    }

    @Override
    protected void targetLogic(){
        super.targetLogic();
        if(target == null) return;
        double distance = getDistanceFromTarget();
        if(distance > getForgetTargetDistance()){
            setTarget(null);
            return;
        }
        mob.setTarget(target);
        if(shouldConstantlyLookAtTarget()) lookAtTarget();
        moveTo();
        targetAttackLogic(distance);
    }

    public boolean shouldConstantlyLookAtTarget(){
        return false;
    }

    public void lookAtTarget(){
        if(target == null) return;
        mob.lookAt(target);
    }

    protected void targetAttackLogic(double distance){
        attemptAttack(target, distance);
    }

    protected int getFindCooldownMax(){
        return 25;
    }

    protected void checkTargetLogic(){
        if(!shouldUpdateTarget()) return;
        if(findTargetCooldown > 0){
            findTargetCooldown--;
            return;
        }
        if(findAndSetTarget(targetCheck)) return;
        findTargetCooldown = getFindCooldownMax();
        target = null;
        mob.setTarget(null);
    }

    protected void sound(){
        if(sounds == null) return;
        sounds.tick();
    }

    public boolean hasBeenRemovedOrDied() {
        return hasBeenRemovedOrDied;
    }

    public Predicate<Entity> getTargetCheck() {
        return targetCheck;
    }

    public void setTargetCheck(Predicate<Entity> targetCheck) {
        this.targetCheck = targetCheck;
    }

    public void setSounds(CruxGoalSounds sounds) {
        this.sounds = sounds;
    }
}