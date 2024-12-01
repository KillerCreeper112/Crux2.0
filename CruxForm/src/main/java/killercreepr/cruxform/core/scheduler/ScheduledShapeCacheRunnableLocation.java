package killercreepr.cruxform.core.scheduler;

import killercreepr.crux.api.math.CruxLocation;
import killercreepr.cruxform.api.scheduler.context.ShapeTickContext;
import killercreepr.cruxform.api.scheduler.context.ShapeTickLocationContext;
import killercreepr.cruxform.api.shape.cache.CreateCachedShape;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ScheduledShapeCacheRunnableLocation extends BukkitRunnable implements ShapeTickLocationContext {
    private final CreateCachedShape cache;
    protected final Consumer<ShapeTickLocationContext> consumer;
    protected final Consumer<ShapeTickContext> tickConsumer;
    protected final Runnable cancelTask;
    private final double maxParEachIteration;
    private double i;
    private int index = -1;
    protected final int maxTicks;

    public ScheduledShapeCacheRunnableLocation(CreateCachedShape cache, Consumer<ShapeTickLocationContext> consumer, Consumer<ShapeTickContext> tickConsumer, Runnable cancelTask, int totalTicksTime) {
        this.cache = cache;
        this.consumer = consumer;
        this.tickConsumer = tickConsumer;
        this.cancelTask = cancelTask;
        maxParEachIteration = (double) cache.size() / (double) totalTicksTime;
        i = maxParEachIteration < 1D ? 0D : maxParEachIteration;
        this.maxTicks = totalTicksTime;
    }

    protected CruxLocation l;
    protected int tick = -1;
    @Override
    public void run() {
        tick++;
        if(cache.size() < 1){
            cancel();
            if(cancelTask != null) cancelTask.run();
            return;
        }

        if(tickConsumer != null) tickConsumer.accept(this);

        if(maxParEachIteration < 1D){
            i++;
            if(i < 1D) return;
        }

        for(int x = (int) i; x > 0; x--){
            index++;
            l = cache.perform(index);
            consumer.accept(this);

            if(index >= cache.size() - 1){
                cancel();
                if(cancelTask != null) cancelTask.run();
                return;
            }
        }
        if(maxParEachIteration < 1D) i--;
    }

    @Override
    public int getIteration() {
        return index;
    }

    @Override
    public @NotNull CruxLocation getLocation() {
        return l;
    }

    @Override
    public int getTick() {
        return tick;
    }

    @Override
    public int getMaxTicks() {
        return maxTicks;
    }

    @Override
    public int getLocationAmount() {
        return cache.size();
    }
}
