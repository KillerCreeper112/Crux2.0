package killercreepr.cruxentities.entity.mob.goal.path;

import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxentities.api.entity.mob.goal.PathTargetMobGoal;
import org.bukkit.entity.Entity;

import java.util.function.Consumer;

public class VariableDistanceGoalNode extends VariableGoalNode {
    protected final double x;
    protected final double y;
    protected final double z;
    protected final double distance;
    protected final double distanceSquared;
    public VariableDistanceGoalNode(Consumer<PathTargetMobGoal> onTick, Consumer<PathTargetMobGoal> onStart, Consumer<PathTargetMobGoal> onFinish, double x, double y, double z, double distance) {
        super(onTick, onStart, onFinish);
        this.x = x;
        this.y = y;
        this.z = z;
        this.distance = distance;
        this.distanceSquared = distance * distance;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public boolean canMoveOn(Entity mob) {
        return CruxMath.distanceSquared(mob.getLocation(), x, y, z) <= distanceSquared;
    }
}
