package killercreepr.cruxform.core.scheduler;

import killercreepr.crux.core.scheduler.BukkitCruxScheduledTask;
import killercreepr.cruxform.api.scheduler.ScheduledShape;
import org.bukkit.scheduler.BukkitRunnable;

public class SimpleScheduledShape extends BukkitCruxScheduledTask implements ScheduledShape {
    public SimpleScheduledShape(BukkitRunnable runnable) {
        super(runnable);
    }

    @Override
    public boolean isRepeatingTask() {
        return true;
    }
}
