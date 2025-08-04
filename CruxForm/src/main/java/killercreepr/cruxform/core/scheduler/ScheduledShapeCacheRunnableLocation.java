package killercreepr.cruxform.core.scheduler;

import killercreepr.crux.api.math.CruxPosition;
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
    protected final int maxTicks;
    protected boolean cancel = false;

    private final double pointsPerTick;
    private double pointProgress = 0.0;
    private int index = -1;

    protected CruxPosition l;
    protected int tick = -1;

    public ScheduledShapeCacheRunnableLocation(CreateCachedShape cache, Consumer<ShapeTickLocationContext> consumer, Consumer<ShapeTickContext> tickConsumer, Runnable cancelTask, int totalTicksTime) {
        this.cache = cache;
        this.consumer = consumer;
        this.tickConsumer = tickConsumer;
        this.cancelTask = cancelTask;
        this.maxTicks = totalTicksTime;

        // Points per tick = how many points to run per tick
        this.pointsPerTick = (double) cache.size() / totalTicksTime;
    }

    public void onCancel() {
        cancel();
        if (cancelTask != null) cancelTask.run();
    }

    @Override
    public void run() {
        tick++;
        if (isCancelled() || cache.size() == 0) {
            onCancel();
            return;
        }

        if (tickConsumer != null) {
            tickConsumer.accept(this);
            if (wasCancelled()) {
                onCancel();
                return;
            }
        }

        // Accumulate fractional progress
        pointProgress += pointsPerTick;
        int stepsThisTick = (int) pointProgress;
        pointProgress -= stepsThisTick;

        // Always process at least 1 point if pointsPerTick < 1
        if (stepsThisTick == 0 && pointsPerTick < 1) {
            stepsThisTick = 1;
        }

        for (int i = 0; i < stepsThisTick; i++) {
            index++;
            if (!cache.has(index)) {
                onCancel();
                return;
            }

            l = cache.perform(index);
            consumer.accept(this);

            if (wasCancelled() || index >= cache.size() - 1) {
                onCancel();
                return;
            }
        }
    }

    @Override
    public int getIteration() {
        return index;
    }

    @Override
    public @NotNull CruxPosition getLocation() {
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

    @Override
    public boolean wasCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean value) {
        cancel = value;
    }
}


/*
package killercreepr.cruxform.core.scheduler;

import killercreepr.crux.api.math.CruxPosition;
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
    protected boolean cancel = false;

    public ScheduledShapeCacheRunnableLocation(CreateCachedShape cache, Consumer<ShapeTickLocationContext> consumer, Consumer<ShapeTickContext> tickConsumer, Runnable cancelTask, int totalTicksTime) {
        this.cache = cache;
        this.consumer = consumer;
        this.tickConsumer = tickConsumer;
        this.cancelTask = cancelTask;
        maxParEachIteration = (double) cache.size() / (double) totalTicksTime;
        i = maxParEachIteration < 1D ? 0D : maxParEachIteration;
        this.maxTicks = totalTicksTime;
    }

    public void onCancel(){
        cancel();
        if(cancelTask != null) cancelTask.run();
    }

    protected CruxPosition l;
    protected int tick = -1;
    @Override
    public void run() {
        tick++;
        if(isCancelled() || cache.size() < 1){
            onCancel();
            return;
        }

        if(tickConsumer != null){
            tickConsumer.accept(this);
            if(wasCancelled()){
                onCancel();
                return;
            }
        }

        if(maxParEachIteration < 1D){
            i++;
            if(i < 1D) return;
        }

        for(int x = (int) i; x > 0; x--){
            index++;
            if(!cache.has(index)){
                onCancel();
                return;
            }
            l = cache.perform(index);
            consumer.accept(this);

            if(wasCancelled() || index >= cache.size() - 1){
                onCancel();
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
    public @NotNull CruxPosition getLocation() {
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

    @Override
    public boolean wasCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean value) {
        cancel = value;
    }
}
*/
