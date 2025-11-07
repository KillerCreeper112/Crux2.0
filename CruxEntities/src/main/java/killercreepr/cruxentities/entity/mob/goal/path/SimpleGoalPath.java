package killercreepr.cruxentities.entity.mob.goal.path;

import killercreepr.cruxentities.api.entity.mob.goal.path.GoalNode;
import killercreepr.cruxentities.api.entity.mob.goal.path.GoalPath;
import org.bukkit.entity.Entity;

import java.util.List;

public class SimpleGoalPath implements GoalPath {
    protected final List<GoalNode> nodes;
    protected int currentIndex = 0;

    public SimpleGoalPath(List<GoalNode> nodes) {
        this.nodes = nodes;
    }
    @Override
    public List<GoalNode> getNodes() {
        return nodes;
    }

    @Override
    public GoalNode getCurrentNode() {
        return currentIndex < 0 || currentIndex >= nodes.size() ? null : nodes.get(currentIndex);
    }

    @Override
    public boolean hasFinished() {
        return currentIndex >= nodes.size();
    }

    @Override
    public boolean canMoveOn(Entity mob) {
        GoalNode node = getCurrentNode();
        return node == null || node.canMoveOn(mob);
    }

    @Override
    public void nextNode() {
        if(hasFinished()) return;
        currentIndex++;
    }

    @Override
    public void finish() {
        currentIndex = nodes.size();
    }

    @Override
    public void reset() {
        currentIndex = 0;
    }

    @Override
    public int getCurrentNodeIndex() {
        return currentIndex;
    }
}
