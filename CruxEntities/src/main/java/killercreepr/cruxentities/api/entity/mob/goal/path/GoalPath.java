package killercreepr.cruxentities.api.entity.mob.goal.path;

import killercreepr.cruxentities.entity.mob.goal.path.SimpleGoalPath;
import org.bukkit.entity.Entity;

import java.util.List;

public interface GoalPath {
    static GoalPath goalPath(List<GoalNode> nodes){
        return new SimpleGoalPath(nodes);
    }

    List<GoalNode> getNodes();
    GoalNode getCurrentNode();
    boolean hasFinished();
    boolean canMoveOn(Entity mob);
    void nextNode();
    void finish();

    void reset();
}
