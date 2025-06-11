package killercreepr.cruxentities.entity.mob.goal.path;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxentities.api.entity.mob.goal.path.GoalNode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class DynamicDistanceGoalNode implements GoalNode {
    protected final Holder<Location> locationHolder;
    protected final double distance;
    protected final double distanceSquared;

    public DynamicDistanceGoalNode(Holder<Location> locationHolder, double distance) {
        this.locationHolder = locationHolder;
        this.distance = distance;
        this.distanceSquared = distance * distance;
    }

    public double getDistanceSquared() {
        return distanceSquared;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public double x() {
        return locationHolder.value().x();
    }

    @Override
    public double y() {
        return locationHolder.value().y();
    }

    @Override
    public double z() {
        return locationHolder.value().z();
    }

    @Override
    public boolean canMoveOn(Entity mob) {
        Location loc = locationHolder.value();
        return CruxMath.distanceSquared(mob.getLocation(), loc.getX(), loc.getY(), loc.getZ()) <= distanceSquared;
    }
}
