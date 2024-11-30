package killercreepr.cruxform.core.scheduler;

import killercreepr.crux.api.math.CruxLocation;
import killercreepr.cruxform.api.shape.cache.CreateCachedShape;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class ScheduledShapeCacheRunnable extends BukkitRunnable {
    private final CreateCachedShape cache;
    protected final Consumer<CruxLocation> consumer;
    protected final Runnable cancelTask;
    private final double maxParEachIteration;
    private double i;
    private int index = -1;

    public ScheduledShapeCacheRunnable(CreateCachedShape cache, Consumer<CruxLocation> consumer, Runnable cancelTask, int totalTicksTime) {
        this.cache = cache;
        this.consumer = consumer;
        this.cancelTask = cancelTask;
        maxParEachIteration = (double) cache.size() / (double) totalTicksTime;
        i = maxParEachIteration < 1D ? 0D : maxParEachIteration;
    }

    @Override
    public void run() {
        if(cache.size() < 1){
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
            CruxLocation l = cache.perform(index);
            consumer.accept(l);

            if(index >= cache.size() - 1){
                cancel();
                if(cancelTask != null) cancelTask.run();
                return;
            }
        }
        if(maxParEachIteration < 1D) i--;
    }
}
