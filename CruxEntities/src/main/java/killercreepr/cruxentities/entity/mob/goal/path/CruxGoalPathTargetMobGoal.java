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

    @Override
    public void setPath(@Nullable GoalPath path) {
        this.path = path;
    }

    public Mob getMob(){
        return goal.getMob();
    }

    public void onPathFinish(){
        if(stopPathFindingWhenFinished) getMob().getPathfinder().stopPathfinding();
    }

    @Override
    public void tick() {
        if(path == null) return;
        if(path.canMoveOn(getMob())){
            path.nextNode();
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
        Mob mob = getMob();
        Location loc = new Location(mob.getWorld(), node.x(), node.y(), node.z());
        mob.getPathfinder().moveTo(loc, getSpeed());
    }
}
