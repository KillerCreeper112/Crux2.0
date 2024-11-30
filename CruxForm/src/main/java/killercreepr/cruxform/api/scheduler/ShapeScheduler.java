package killercreepr.cruxform.api.scheduler;

import killercreepr.crux.api.scheduler.CruxScheduledTask;
import killercreepr.cruxform.api.scheduler.context.ShapeTickContext;
import killercreepr.cruxform.api.shape.CreateShape;
import killercreepr.cruxform.core.scheduler.SimpleScheduledShape;

import java.util.function.Consumer;

public interface ShapeScheduler {
    static Builder builder(){
        return new SimpleScheduledShape.Builder();
    }

    CruxScheduledTask schedule(int totalTicksTime);
    CruxScheduledTask scheduleAsync(int totalTicksTime);

    interface Builder{
        Builder locationTick(Consumer<ShapeTickContext> locationTick);
        Builder endTask(Runnable endTask);
        Builder shape(CreateShape shape);
        ShapeScheduler build();
        ShapeScheduler buildCached();
    }
}
