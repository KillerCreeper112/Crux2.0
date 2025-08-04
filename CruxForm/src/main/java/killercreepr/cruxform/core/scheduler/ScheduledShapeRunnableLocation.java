package killercreepr.cruxform.core.scheduler;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.cruxform.api.scheduler.context.ShapeTickContext;
import killercreepr.cruxform.api.scheduler.context.ShapeTickLocationContext;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class ScheduledShapeRunnableLocation extends BukkitRunnable implements ShapeTickLocationContext {
    protected final Consumer<ShapeTickLocationContext> locationConsumer;
    protected final Consumer<ShapeTickContext> tickConsumer;
    protected final Runnable cancelTask;
    private final List<CruxPosition> list;
    protected final int totalTicks;
    protected boolean cancel = false;

    private final double pointsPerTick;
    private double progress = 0.0;
    private int index = 0;
    private int tick = 0;
    private CruxPosition l;

    public ScheduledShapeRunnableLocation(
        Consumer<ShapeTickLocationContext> locationConsumer,
        Consumer<ShapeTickContext> tickConsumer,
        Runnable cancelTask,
        List<CruxPosition> list,
        int totalTicksTime
    ) {
        this.locationConsumer = locationConsumer;
        this.tickConsumer = tickConsumer;
        this.cancelTask = cancelTask;
        this.list = list;
        this.totalTicks = totalTicksTime;
        this.pointsPerTick = (double) list.size() / totalTicksTime;
    }

    @Override
    public void run() {
        if (list.isEmpty() || cancel) {
            onCancel();
            return;
        }

        if (tickConsumer != null) {
            tickConsumer.accept(this);
            if (cancel) {
                onCancel();
                return;
            }
        }

        tick++;

        // Accumulate progress
        progress += pointsPerTick;
        int pointsToRun = (int) Math.floor(progress);
        progress -= pointsToRun;

        for (int i = 0; i < pointsToRun; i++) {
            if (index >= list.size()) {
                onCancel(); // All points processed
                return;
            }

            l = list.get(index);
            locationConsumer.accept(this);
            index++;

            if (cancel) {
                onCancel();
                return;
            }
        }

        // Failsafe: in case ticks end before all points are shown
        if (tick >= totalTicks && index >= list.size()) {
            onCancel();
        }
    }

    public void onCancel() {
        cancel();
        if (cancelTask != null) cancelTask.run();
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
        return totalTicks;
    }

    @Override
    public int getLocationAmount() {
        return list.size();
    }

    @Override
    public boolean wasCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean value) {
        this.cancel = value;
    }
}



/*
package killercreepr.cruxform.core.scheduler;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.cruxform.api.scheduler.context.ShapeTickContext;
import killercreepr.cruxform.api.scheduler.context.ShapeTickLocationContext;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class ScheduledShapeRunnableLocation extends BukkitRunnable implements ShapeTickLocationContext {
    protected final Consumer<ShapeTickLocationContext> locationConsumer;
    protected final Consumer<ShapeTickContext> tickConsumer;
    protected final Runnable cancelTask;
    private final List<CruxPosition> list;
    protected final int totalTicks;
    protected boolean cancel = false;

    public ScheduledShapeRunnableLocation(Consumer<ShapeTickLocationContext> locationConsumer, Consumer<ShapeTickContext> tickConsumer, Runnable cancelTask, List<CruxPosition> list, int totalTicksTime) {
        this.locationConsumer = locationConsumer;
        this.tickConsumer = tickConsumer;
        this.cancelTask = cancelTask;
        this.list = list;
        this.maxParEachIteration = (double) list.size() / (double) totalTicksTime;
        this.i = maxParEachIteration < 1D ? 0D : maxParEachIteration;
        this.totalTicks = totalTicksTime;
    }

    public void onCancel(){
        cancel();
        if(cancelTask != null) cancelTask.run();
    }

    private final double maxParEachIteration;
    private double i;
    private int index = -1;
    protected CruxPosition l;
    protected int tick = -1;
    @Override
    public void run(){
        tick++;
        if(list.isEmpty()){
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
            if(index >= list.size()){
                onCancel();
                return;
            }
            l = list.get(index);
            locationConsumer.accept(this);

            if(wasCancelled() || index >= list.size() - 1){
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
        return totalTicks;
    }

    @Override
    public int getLocationAmount() {
        return list.size();
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
