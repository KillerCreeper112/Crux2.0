package killercreepr.cruxadvancements.advancement.objective.condition;

import org.jetbrains.annotations.NotNull;

public interface ObjectiveConditions {
    boolean test(@NotNull ConditionContext ctx);
}
