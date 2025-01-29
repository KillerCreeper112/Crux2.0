package killercreepr.cruxentities.entity.mob.goal.path;

import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxentities.api.entity.mob.goal.path.GoalNode;
import org.bukkit.entity.Entity;

public class DistanceGoalNode implements GoalNode {
    protected final double x;
    protected final double y;
    protected final double z;
    protected final double distance;
    protected final double distanceSquared;

    public DistanceGoalNode(double x, double y, double z, double distance) {
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
