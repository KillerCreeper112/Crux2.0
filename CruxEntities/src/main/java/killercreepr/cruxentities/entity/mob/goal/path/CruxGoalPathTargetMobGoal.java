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

    public CruxGoalPathTargetMobGoal(CruxGoalBase goal, double speed) {
        this.goal = goal;
        this.speed = speed;
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

    @Override
    public void tick() {
        if(path == null) return;
        if(path.canMoveOn(getMob())){
            path.nextNode();
        }
        if(path.hasFinished()){
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
