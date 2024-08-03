package killercreepr.cruxadvancements.advancement.objective;

import killercreepr.cruxadvancements.advancement.objective.condition.ConditionContext;
import killercreepr.cruxadvancements.advancement.objective.condition.ObjectiveConditions;
import killercreepr.cruxadvancements.advancement.objective.progress.ObjectiveProgress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface AdvancementObjective {
    default boolean canTrigger(@NotNull ConditionContext ctx){
        ObjectiveConditions conditions = getConditions();
        if(conditions==null) return true;
        return conditions.test(ctx);
    }

    @NotNull
    String getCriterion();
    boolean isDone(@NotNull ObjectiveProgress progress);
    boolean shouldUpdateAdvancement(@NotNull ObjectiveProgress progress);
    @NotNull ObjectiveProgress createNewProgress();
    @Nullable
    ObjectiveConditions getConditions();
}
