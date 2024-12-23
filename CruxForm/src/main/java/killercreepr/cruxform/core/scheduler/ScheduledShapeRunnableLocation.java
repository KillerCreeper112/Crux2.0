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

    public ScheduledShapeRunnableLocation(Consumer<ShapeTickLocationContext> locationConsumer, Consumer<ShapeTickContext> tickConsumer, Runnable cancelTask, List<CruxPosition> list, int totalTicksTime) {
        this.locationConsumer = locationConsumer;
        this.tickConsumer = tickConsumer;
        this.cancelTask = cancelTask;
        this.list = list;
        this.maxParEachIteration = (double) list.size() / (double) totalTicksTime;
        this.i = maxParEachIteration < 1D ? 0D : maxParEachIteration;
        this.totalTicks = totalTicksTime;
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
            l = list.get(index);
            locationConsumer.accept(this);

            if(index >= list.size() - 1){
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
}
