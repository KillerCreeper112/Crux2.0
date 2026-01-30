package killercreepr.cruxentities.entity.mob.goal.path;

import killercreepr.cruxentities.api.entity.mob.goal.PathTargetMobGoal;
import killercreepr.cruxentities.api.entity.mob.goal.path.GoalNode;
import killercreepr.cruxentities.api.entity.mob.goal.path.GoalPath;
import killercreepr.cruxentities.api.event.EntityPathTargetFinishEvent;
import killercreepr.cruxentities.api.event.EntityPathTargetStartEvent;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.Nullable;

public class SimplePathTargetMobGoal implements PathTargetMobGoal {
    protected final Mob mob;
    protected final double speed;
    protected final boolean stopPathFindingWhenFinished;

    public SimplePathTargetMobGoal(Mob mob, double speed, boolean stopPathFindingWhenFinished) {
        this.mob = mob;
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
        if(path != null){
            GoalNode current = path.getCurrentNode();
            if(current != null) current.onFinish(this);
        }
        this.path = path;
        if(this.path != null){
            new EntityPathTargetStartEvent(mob, this).callEvent();
        }
    }

    public void onPathFinish(){
        if(stopPathFindingWhenFinished) mob.getPathfinder().stopPathfinding();
        new EntityPathTargetFinishEvent(mob, this).callEvent();
    }

    @Override
    public void tick() {
        if(path==null) return;
        if(path.canMoveOn(mob)){
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

        Location loc = new Location(mob.getWorld(), node.x(), node.y(), node.z());
        mob.getPathfinder().moveTo(loc, getSpeed());
    }
}
