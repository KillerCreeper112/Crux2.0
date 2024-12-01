package killercreepr.cruxform.core.scheduler;

import killercreepr.crux.api.scheduler.CruxScheduledTask;
import killercreepr.crux.core.Crux;
import killercreepr.cruxform.api.scheduler.ShapeScheduler;
import killercreepr.cruxform.api.scheduler.context.ShapeTickContext;
import killercreepr.cruxform.api.scheduler.context.ShapeTickLocationContext;
import killercreepr.cruxform.api.shape.CreateShape;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SimpleShapeScheduler implements ShapeScheduler {
    protected final @NotNull CreateShape shape;
    protected final @NotNull Consumer<ShapeTickLocationContext> consumer;
    protected final @NotNull Consumer<ShapeTickContext> tickConsumer;
    protected final @Nullable Runnable endTask;

    public SimpleShapeScheduler(@NotNull CreateShape shape, @NotNull Consumer<ShapeTickLocationContext> consumer, @NotNull Consumer<ShapeTickContext> tickConsumer, @Nullable Runnable endTask) {
        this.shape = shape;
        this.consumer = consumer;
        this.tickConsumer = tickConsumer;
        this.endTask = endTask;
    }

    @Override
    public CruxScheduledTask schedule(int totalTicksTime) {
        BukkitRunnable runnable = new ScheduledShapeRunnableLocation(
            consumer, tickConsumer, endTask, shape.generateList(), totalTicksTime
        );
        runnable.runTaskTimer(Crux.getMainPlugin(), 0L, 1L);
        return new SimpleScheduledShape(runnable);
    }

    @Override
    public CruxScheduledTask scheduleAsync(int totalTicksTime) {
        BukkitRunnable runnable = new ScheduledShapeRunnableLocation(
            consumer, tickConsumer, endTask, shape.generateList(), totalTicksTime
        );
        runnable.runTaskTimerAsynchronously(Crux.getMainPlugin(), 0L, 1L);
        return new SimpleScheduledShape(runnable);
    }


}
