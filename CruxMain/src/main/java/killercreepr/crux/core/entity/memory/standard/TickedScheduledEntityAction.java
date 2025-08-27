package killercreepr.crux.core.entity.memory.standard;

import killercreepr.crux.api.entity.memory.standard.ScheduledEntityAction;
import killercreepr.crux.core.util.CruxMath;
import org.bukkit.entity.Entity;

public abstract class TickedScheduledEntityAction implements ScheduledEntityAction {
    protected final int ticks;
    protected final long time;

    public TickedScheduledEntityAction(int ticks, long time) {
        this.ticks = ticks;
        this.time = time;
    }
    public TickedScheduledEntityAction(int ticks) {
        this(ticks, System.currentTimeMillis());
    }

    @Override
    public boolean isReady(Entity e) {
        return !CruxMath.hasOccurredWithin(time, ticks);
    }
}
