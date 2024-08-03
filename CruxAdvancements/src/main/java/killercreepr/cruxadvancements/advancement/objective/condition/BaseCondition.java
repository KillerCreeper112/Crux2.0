package killercreepr.cruxadvancements.advancement.objective.condition;

import org.jetbrains.annotations.NotNull;

public abstract class BaseCondition implements ObjectiveCondition{
    protected final @NotNull String target;
    public BaseCondition(@NotNull String target) {
        this.target = target;
    }

    @Override
    public @NotNull String getTarget() {
        return target;
    }
}
