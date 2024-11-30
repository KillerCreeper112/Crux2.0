package killercreepr.cruxform.core.scheduler;

import killercreepr.crux.api.math.CruxLocation;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.function.Consumer;

public class ScheduledShapeRunnable extends BukkitRunnable {
    protected final Consumer<CruxLocation> locationConsumer;
    protected final Runnable cancelTask;
    private final List<CruxLocation> list;

    public ScheduledShapeRunnable(Consumer<CruxLocation> locationConsumer, Runnable cancelTask, List<CruxLocation> list, int totalTicksTime) {
        this.locationConsumer = locationConsumer;
        this.cancelTask = cancelTask;
        this.list = list;
        this.maxParEachIteration = (double) list.size() / (double) totalTicksTime;
        this.i = maxParEachIteration < 1D ? 0D : maxParEachIteration;
    }

    private final double maxParEachIteration;
    private double i;
    private int index = -1;
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
            CruxLocation l = list.get(index);
            locationConsumer.accept(l);

            if(index >= list.size() - 1){
                cancel();
                if(cancelTask != null) cancelTask.run();
                return;
            }
        }
        if(maxParEachIteration < 1D) i--;
    }
}
