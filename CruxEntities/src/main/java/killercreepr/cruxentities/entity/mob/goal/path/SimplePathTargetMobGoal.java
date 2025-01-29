package killercreepr.cruxentities.entity.mob.goal.path;

import killercreepr.cruxentities.api.entity.mob.goal.PathTargetMobGoal;
import killercreepr.cruxentities.api.entity.mob.goal.path.GoalNode;
import killercreepr.cruxentities.api.entity.mob.goal.path.GoalPath;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.Nullable;

public class SimplePathTargetMobGoal implements PathTargetMobGoal {
    protected final Mob mob;
    protected final double speed;

    public SimplePathTargetMobGoal(Mob mob, double speed) {
        this.mob = mob;
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

    @Override
    public void tick() {
        if(path.canMoveOn(mob)){
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
        Location loc = new Location(mob.getWorld(), node.x(), node.y(), node.z());
        mob.getPathfinder().moveTo(loc, getSpeed());
    }
}
