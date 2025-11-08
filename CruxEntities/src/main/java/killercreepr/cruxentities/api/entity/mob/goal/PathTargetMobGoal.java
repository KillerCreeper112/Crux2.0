package killercreepr.cruxentities.api.entity.mob.goal;

import com.destroystokyo.paper.entity.ai.Goal;
import killercreepr.cruxentities.api.entity.mob.goal.path.GoalPath;
import killercreepr.cruxentities.entity.mob.goal.CruxGoalBase;
import killercreepr.cruxentities.entity.mob.goal.ICruxGoal;
import killercreepr.cruxentities.entity.mob.goal.path.CruxGoalPathTargetMobGoal;
import killercreepr.cruxentities.entity.mob.goal.path.SimplePathTargetMobGoal;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.Nullable;

public interface PathTargetMobGoal {
    static PathTargetMobGoal pathTargetMobGoal(Mob mob) {
        return pathTargetMobGoal(mob, 1D);
    }
    static PathTargetMobGoal pathTargetMobGoal(Mob mob, double speed) {
        return pathTargetMobGoal(mob, speed, true);
    }

    static PathTargetMobGoal pathTargetMobGoal(Mob mob, double speed, boolean stopPathFindingWhenFinished) {
        return new SimplePathTargetMobGoal(mob, speed, stopPathFindingWhenFinished);
    }

    static PathTargetMobGoal pathTargetMobGoal(CruxGoalBase goal) {
        return pathTargetMobGoal(goal, 1D);
    }
    static PathTargetMobGoal pathTargetMobGoal(CruxGoalBase goal, double speed) {
        return pathTargetMobGoal(goal, speed, true);
    }

    static PathTargetMobGoal pathTargetMobGoal(CruxGoalBase goal, double speed, boolean stopPathFindingWhenFinished) {
        return new CruxGoalPathTargetMobGoal(goal, speed, stopPathFindingWhenFinished);
    }

    @Nullable GoalPath getPath();
    void setPath(@Nullable GoalPath path);
    default boolean hasPath() {
        return getPath() != null;
    }
    void tick();

    interface Goaled extends PathTargetMobGoal{
        ICruxGoal getGoal();
    }
}
