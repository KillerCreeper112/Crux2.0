package killercreepr.cruxentities.api.event;

import killercreepr.cruxentities.api.entity.mob.goal.PathTargetMobGoal;
import org.bukkit.entity.Entity;

public class EntityPathTargetStartEvent extends EntityPathTargetEvent{
  public EntityPathTargetStartEvent(Entity entity, PathTargetMobGoal goal) {
    super(entity, goal);
  }
}
