package killercreepr.cruxadvancements.advancement.objective;

import killercreepr.cruxadvancements.advancement.objective.progress.ObjectiveProgress;
import org.jetbrains.annotations.NotNull;

public interface AdvancementObjective {
    @NotNull
    String getCriterion();
    boolean isDone(@NotNull ObjectiveProgress progress);
}
