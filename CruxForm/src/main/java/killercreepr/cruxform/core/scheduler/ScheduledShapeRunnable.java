package killercreepr.cruxform.core.scheduler;

import killercreepr.crux.api.math.CruxLocation;
import killercreepr.cruxform.api.scheduler.context.ShapeTickContext;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class ScheduledShapeRunnable extends BukkitRunnable implements ShapeTickContext {
    protected final Consumer<ShapeTickContext> locationConsumer;
    protected final Runnable cancelTask;
    private final List<CruxLocation> list;

    public ScheduledShapeRunnable(Consumer<ShapeTickContext> locationConsumer, Runnable cancelTask, List<CruxLocation> list, int totalTicksTime) {
        this.locationConsumer = locationConsumer;
        this.cancelTask = cancelTask;
        this.list = list;
        this.maxParEachIteration = (double) list.size() / (double) totalTicksTime;
        this.i = maxParEachIteration < 1D ? 0D : maxParEachIteration;
    }

    private final double maxParEachIteration;
    private double i;
    private int index = -1;
    protected CruxLocation l;
    @Override
    public void run(){
        if(list.isEmpty()){
            cancel();
            if(cancelTask != null) cancelTask.run();
            return;
        }
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
    public int getTick() {
        return index;
    }

    @Override
    public @NotNull CruxLocation getLocation() {
        return l;
    }

    @Override
    public int getSize() {
        return list.size();
    }
}
