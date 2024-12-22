package killercreepr.cruxentities.entity.mob.goal;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ICruxGoal {
    @Nullable LivingEntity getTarget();
    void setTarget(@Nullable LivingEntity newTarget);
    boolean shouldUpdateTarget();
    boolean isValidAttackerTarget(@NotNull LivingEntity target);
    boolean isValidNaturalTarget(@NotNull LivingEntity target);
    boolean isValidTarget(@NotNull LivingEntity target);
    boolean isValidHitTarget(@NotNull Entity target);
    void forgetTarget();
    boolean knowsWhereTargetIs();
    boolean hasLineOfSight(@NotNull Entity e);
    boolean canAttack();
}
