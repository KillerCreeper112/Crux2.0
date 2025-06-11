package killercreepr.cruxentities.api.entity.mob.goal.path;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.cruxentities.entity.mob.goal.path.DistanceGoalNode;
import killercreepr.cruxentities.entity.mob.goal.path.DynamicDistanceGoalNode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public interface GoalNode {
    static GoalNode distanceGoalNode(double x, double y, double z, double distance){
        return new DistanceGoalNode(x, y, z, distance);
    }
    static GoalNode distanceGoalNode(Location loc, double distance){
        return distanceGoalNode(loc.getX(), loc.getY(), loc.getZ(), distance);
    }
    static GoalNode distanceGoalNode(Vector loc, double distance){
        return distanceGoalNode(loc.getX(), loc.getY(), loc.getZ(), distance);
    }
    static GoalNode distanceGoalNode(CruxPosition loc, double distance){
        return distanceGoalNode(loc.x(), loc.y(), loc.z(), distance);
    }

    static GoalNode dynamicDistanceGoalNode(@NotNull Holder<Location> locationHolder, double distance){
        return new DynamicDistanceGoalNode(locationHolder, distance);
    }

    double x();
    double y();
    double z();
    boolean canMoveOn(Entity mob);
}
