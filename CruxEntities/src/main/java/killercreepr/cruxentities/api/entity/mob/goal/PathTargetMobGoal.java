package killercreepr.cruxentities.api.entity.mob.goal;

import killercreepr.cruxentities.api.entity.mob.goal.path.GoalPath;
import killercreepr.cruxentities.entity.mob.goal.path.SimplePathTargetMobGoal;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.Nullable;

public interface PathTargetMobGoal {
    static PathTargetMobGoal pathTargetMobGoal(Mob mob) {
        return pathTargetMobGoal(mob, 1D);
    }
    static PathTargetMobGoal pathTargetMobGoal(Mob mob, double speed) {
        return new SimplePathTargetMobGoal(mob, speed);
    }

    @Nullable GoalPath getPath();
    void setPath(@Nullable GoalPath path);
    void tick();
}
