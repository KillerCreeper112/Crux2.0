package killercreepr.cruxadvancements.advancement.objective.condition;

import org.jetbrains.annotations.NotNull;

public interface ObjectiveCondition {
    @NotNull String getTarget();
    boolean test(@NotNull ConditionContext ctx);
}
