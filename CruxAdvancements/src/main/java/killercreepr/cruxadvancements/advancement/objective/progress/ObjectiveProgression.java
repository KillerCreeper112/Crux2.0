package killercreepr.cruxadvancements.advancement.objective.progress;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ObjectiveProgression {
    @NotNull ObjectiveProgress getProgress(@NotNull String taskID);
    @Nullable ObjectiveProgress getProgressIfPresent(@NotNull String taskID);
    void setProgress(@NotNull String taskID, @Nullable ObjectiveProgress progress);
    //task_id -> progress
    @NotNull
    Map<String, ObjectiveProgress> getProgressMap();
}
