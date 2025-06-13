package killercreepr.cruxentities.entity.mob.goal.path;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxentities.api.entity.mob.goal.PathTargetMobGoal;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.function.Consumer;

public class VariableDynamicDistanceGoalNode extends VariableGoalNode {
    protected final Holder<Location> locationHolder;
    protected final double distance;
    protected final double distanceSquared;
    public VariableDynamicDistanceGoalNode(Consumer<PathTargetMobGoal> onTick, Consumer<PathTargetMobGoal> onStart, Consumer<PathTargetMobGoal> onFinish, Holder<Location> location, double distance) {
        super(onTick, onStart, onFinish);
        this.locationHolder = location;
        this.distance = distance;
        this.distanceSquared = distance * distance;
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
