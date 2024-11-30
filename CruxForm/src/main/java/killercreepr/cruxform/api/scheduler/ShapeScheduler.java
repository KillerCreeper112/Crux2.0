package killercreepr.cruxform.api.scheduler;

import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.api.scheduler.CruxScheduledTask;
import killercreepr.crux.core.Crux;
import killercreepr.cruxform.api.shape.CreateShape;
import killercreepr.cruxform.core.scheduler.SimpleScheduledShape;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public interface ShapeScheduler {
    static BukkitRunnable buildRunnable(@NotNull CreateShape on, @NotNull Consumer<CruxLocation> consumer, int totalTicksTime){
        return new BukkitRunnable() {
            private final List<CruxLocation> list = on.generateList();
            private final double maxParEachIteration = (double) list.size() / (double) totalTicksTime;
            private double i = maxParEachIteration < 1D ? 0D : maxParEachIteration;
            private int index = -1;
            @Override
            public void run() {
                if(list.isEmpty()){
                    cancel();
                    return;
                }
                if(maxParEachIteration < 1D){
                    i++;
                    if(i < 1D) return;
                }

                for(int x = (int) i; x > 0; x--){
                    index++;
                    CruxLocation l = list.get(index);
                    consumer.accept(l);

                    if(index >= list.size() - 1){
                        cancel();
                        return;
                    }
                }
                if(maxParEachIteration < 1D) i--;
            }
        };
    }

    static CruxScheduledTask schedule(@NotNull CreateShape on, @NotNull Consumer<CruxLocation> consumer, int totalTicksTime){
        BukkitRunnable run = buildRunnable(on, consumer, totalTicksTime);
        run.runTaskTimer(Crux.getMainPlugin(), 0L, 1L);
        return new SimpleScheduledShape(run);
    }

    static CruxScheduledTask scheduleAsync(@NotNull CreateShape on, @NotNull Consumer<CruxLocation> consumer, int totalTicksTime){
        BukkitRunnable run = buildRunnable(on, consumer, totalTicksTime);
        run.runTaskTimerAsynchronously(Crux.getMainPlugin(), 0L, 1L);
        return new SimpleScheduledShape(run);
    }
}
