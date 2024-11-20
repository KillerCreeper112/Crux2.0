package killercreepr.cruxadvancements.core.advancement.objective.progress;

import killercreepr.cruxadvancements.api.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.api.advancement.objective.progress.ObjectiveProgress;
import killercreepr.cruxadvancements.api.advancement.objective.progress.ObjectiveProgression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SimpleObjectiveProgression implements ObjectiveProgression {
    protected final @NotNull Map<String, ObjectiveProgress> progressionMap = new HashMap<>();
    protected final @NotNull ObjectiveAdvancement advancement;

    public SimpleObjectiveProgression(@NotNull ObjectiveAdvancement advancement) {
        this.advancement = advancement;
    }

    @Override
    public @NotNull ObjectiveProgress getProgress(@NotNull String taskID) {
        return progressionMap.computeIfAbsent(taskID, id -> advancement.getObjective(taskID).createNewProgress());
    }

    @Override
    public @Nullable ObjectiveProgress getProgressIfPresent(@NotNull String taskID) {
        return progressionMap.get(taskID);
    }

    @Override
    public void setProgress(@NotNull String taskID, @Nullable ObjectiveProgress progress) {
        if(progress==null) progressionMap.remove(taskID);
        else progressionMap.put(taskID, progress);
    }

    @Override
    public @NotNull Map<String, ObjectiveProgress> getProgressMap() {
        return progressionMap;
    }
}
