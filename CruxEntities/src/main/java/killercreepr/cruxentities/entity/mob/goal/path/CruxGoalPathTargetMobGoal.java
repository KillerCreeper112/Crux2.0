package killercreepr.cruxentities.entity.mob.goal.path;

import killercreepr.cruxentities.api.entity.mob.goal.PathTargetMobGoal;
import killercreepr.cruxentities.api.entity.mob.goal.path.GoalNode;
import killercreepr.cruxentities.api.entity.mob.goal.path.GoalPath;
import killercreepr.cruxentities.entity.mob.goal.CruxGoalBase;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.Nullable;

public class CruxGoalPathTargetMobGoal implements PathTargetMobGoal {
    protected final CruxGoalBase goal;
    protected final double speed;
    protected final boolean stopPathFindingWhenFinished;

    public CruxGoalPathTargetMobGoal(CruxGoalBase goal, double speed, boolean stopPathFindingWhenFinished) {
        this.goal = goal;
        this.speed = speed;
        this.stopPathFindingWhenFinished = stopPathFindingWhenFinished;
    }

    protected GoalPath path;
    @Override
    public @Nullable GoalPath getPath() {
        return path;
    }

    public Mob getMob(){
        return goal.getMob();
    }

    @Override
    public void setPath(@Nullable GoalPath path) {
        if(path != null){
            GoalNode current = path.getCurrentNode();
            if(current != null) current.onFinish(this);
        }
        this.path = path;
    }

    public void onPathFinish(){
        if(stopPathFindingWhenFinished) getMob().getPathfinder().stopPathfinding();
    }

    @Override
    public void tick() {
        if(path.canMoveOn(getMob())){
            GoalNode current = path.getCurrentNode();
            if(current != null) current.onFinish(this);
            path.nextNode();

            current = path.getCurrentNode();
            if(current != null) current.onStart(this);
        }
        if(path.hasFinished()){
            onPathFinish();
            setPath(null);
            return;
        }
        GoalNode node = path.getCurrentNode();
        if(node == null) return;
        onCurrentNodeTick(node);
    }

    public double getSpeed(){
        return speed;
    }

    public void onCurrentNodeTick(GoalNode node){
        node.onTick(this);

        Location loc = new Location(getMob().getWorld(), node.x(), node.y(), node.z());
        getMob().getPathfinder().moveTo(loc, getSpeed());
    }
}
