package killercreepr.cruxform.core.scheduler;

import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.core.scheduler.BukkitCruxScheduledTask;
import killercreepr.cruxform.api.scheduler.ScheduledShape;
import killercreepr.cruxform.api.scheduler.ShapeScheduler;
import killercreepr.cruxform.api.shape.CreateShape;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class SimpleScheduledShape extends BukkitCruxScheduledTask implements ScheduledShape {
    public SimpleScheduledShape(BukkitRunnable runnable) {
        super(runnable);
    }

    @Override
    public boolean isRepeatingTask() {
        return true;
    }

    public static class Builder implements ShapeScheduler.Builder {
        protected Consumer<CruxLocation> locationTick;
        protected Runnable endTask;
        protected CreateShape shape;

        @Override
        public ShapeScheduler.Builder locationTick(Consumer<CruxLocation> locationTick) {
            this.locationTick = locationTick;
            return this;
        }

        @Override
        public ShapeScheduler.Builder endTask(Runnable endTask) {
            this.endTask = endTask;
            return this;
        }

        @Override
        public ShapeScheduler.Builder shape(CreateShape shape) {
            this.shape = shape;
            return this;
        }

        @Override
        public ShapeScheduler build() {
            return new SimpleShapeScheduler(shape, locationTick, endTask);
        }

        @Override
        public ShapeScheduler buildCached() {
            return new SimpleShapeCachedScheduler(shape, locationTick, endTask);
        }
    }
}
