package killercreepr.crux.core.scheduler;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import killercreepr.crux.api.scheduler.CruxScheduledTask;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Experimental
public class BukkitCruxScheduledTask implements CruxScheduledTask {
    protected final BukkitRunnable runnable;
    public BukkitCruxScheduledTask(BukkitRunnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public boolean isRepeatingTask() {
        return false;
    }

    /**
     * Attempts to cancel this task, returning the result of the attempt. In all cases, if the task is currently
     * being executed no attempt is made to halt the task, however any executions in the future are halted.
     *
     * @return the result of the cancellation attempt.
     */
    @NotNull
    @Override
    public ScheduledTask.CancelledState cancel() {
        runnable.cancel();
        return ScheduledTask.CancelledState.CANCELLED_BY_CALLER;
    }

    /**
     * Returns the current execution state of this task.
     *
     * @return the current execution state of this task.
     */
    @Override
    public @NotNull ScheduledTask.ExecutionState getExecutionState() {
        return runnable.isCancelled() ? ScheduledTask.ExecutionState.CANCELLED : ScheduledTask.ExecutionState.RUNNING;
    }
}
