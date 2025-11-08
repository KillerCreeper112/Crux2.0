package killercreepr.cruxentities.api.entity.mob.goal.path;

import killercreepr.cruxentities.entity.mob.goal.path.SimpleGoalPath;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.ApiStatus;

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
    GoalNode getNextNode();
    void finish();

    void reset();

    @ApiStatus.Internal
    int getCurrentNodeIndex();
}
