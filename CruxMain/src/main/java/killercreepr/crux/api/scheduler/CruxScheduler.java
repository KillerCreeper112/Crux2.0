package killercreepr.crux.api.scheduler;

import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface CruxScheduler {
    CruxScheduler runTask(@NotNull Runnable task);
    CruxScheduler runTask(@NotNull Consumer<? super BukkitTask> task);
    CruxScheduler runTaskLater(@NotNull Runnable task, long delay);
    CruxScheduler runTaskLater(@NotNull Consumer<? super BukkitTask> task, long delay);

    CruxScheduler runTaskAsync(@NotNull Runnable task);
    CruxScheduler runTaskAsync(@NotNull Consumer<? super BukkitTask> task);
    CruxScheduler runTaskLaterAsync(@NotNull Runnable task, long delay);
    CruxScheduler runTaskLaterAsync(@NotNull Consumer<? super BukkitTask> task, long delay);
}
