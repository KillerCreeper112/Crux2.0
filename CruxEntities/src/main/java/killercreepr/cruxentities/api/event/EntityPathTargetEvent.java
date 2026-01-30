package killercreepr.cruxentities.api.event;

import killercreepr.crux.core.Crux;
import killercreepr.cruxentities.api.entity.mob.goal.PathTargetMobGoal;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class EntityPathTargetEvent extends Event {
  public static final HandlerList HANDLER_LIST =  new HandlerList();
  protected final Entity entity;
  protected final PathTargetMobGoal goal;

  public PathTargetMobGoal getGoal() {
    return goal;
  }

  public Entity getEntity() {
    return entity;
  }

  /**
   * The default constructor is defined for cleaner code. This constructor
   * assumes the event is synchronous.
   */
  public EntityPathTargetEvent(Entity entity, PathTargetMobGoal goal) {
    super(!Crux.isPrimaryThread());
    this.entity = entity;
    this.goal = goal;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLER_LIST;
  }
  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
}
