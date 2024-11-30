package killercreepr.crux.api.scheduler;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.jetbrains.annotations.NotNull;

public interface CruxScheduledTask {
    boolean isRepeatingTask();

    /**
     * Attempts to cancel this task, returning the result of the attempt. In all cases, if the task is currently
     * being executed no attempt is made to halt the task, however any executions in the future are halted.
     * @return the result of the cancellation attempt.
     */
    @NotNull
    ScheduledTask.CancelledState cancel();

    /**
     * Returns the current execution state of this task.
     * @return the current execution state of this task.
     */
    @NotNull
    ScheduledTask.ExecutionState getExecutionState();

    /**
     * Returns whether the current execution state is {@link ScheduledTask.ExecutionState#CANCELLED} or {@link ScheduledTask.ExecutionState#CANCELLED_RUNNING}.
     * @return whether the current execution state is {@link ScheduledTask.ExecutionState#CANCELLED} or {@link ScheduledTask.ExecutionState#CANCELLED_RUNNING}.
     */
    default boolean isCancelled() {
        final ScheduledTask.ExecutionState state = this.getExecutionState();
        return state == ScheduledTask.ExecutionState.CANCELLED || state == ScheduledTask.ExecutionState.CANCELLED_RUNNING;
    }

    /**
     * Represents the result of attempting to cancel a task.
     */
    enum CancelledState {
        /**
         * The task (repeating or not) has been successfully cancelled by the caller thread. The task is not executing
         * currently, and it will not begin execution in the future.
         */
        CANCELLED_BY_CALLER,
        /**
         * The task (repeating or not) is already cancelled. The task is not executing currently, and it will not
         * begin execution in the future.
         */
        CANCELLED_ALREADY,

        /**
         * The task is not a repeating task, and could not be cancelled because the task is being executed.
         */
        RUNNING,
        /**
         * The task is not a repeating task, and could not be cancelled because the task has already finished execution.
         */
        ALREADY_EXECUTED,

        /**
         * The caller thread successfully stopped future executions of a repeating task, but the task is currently
         * being executed.
         */
        NEXT_RUNS_CANCELLED,

        /**
         * The repeating task's future executions are cancelled already, but the task is currently
         * being executed.
         */
        NEXT_RUNS_CANCELLED_ALREADY,
    }

    /**
     * Represents the current execution state of the task.
     */
    enum ExecutionState {
        /**
         * The task is currently not executing, but may begin execution in the future.
         */
        IDLE,

        /**
         * The task is currently executing.
         */
        RUNNING,

        /**
         * The task is not repeating, and the task finished executing.
         */
        FINISHED,

        /**
         * The task is not executing and will not begin execution in the future. If this task is not repeating, then
         * this task was never executed.
         */
        CANCELLED,

        /**
         * The task is repeating and currently executing, but future executions are cancelled and will not occur.
         */
        CANCELLED_RUNNING;
    }
}
