package killercreepr.cruxadvancements.advancement.objective;

import killercreepr.cruxadvancements.advancement.objective.condition.ObjectiveConditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SimpleAdvancementObjective implements AdvancementObjective {
    protected final @NotNull String criterion;
    protected final @Nullable ObjectiveConditions conditions;
    public SimpleAdvancementObjective(@NotNull String criterion, @Nullable ObjectiveConditions conditions) {
        this.criterion = criterion;
        this.conditions = conditions;
    }

    @Override
    public @NotNull String getCriterion() {
        return criterion;
    }

    @Override
    public @Nullable ObjectiveConditions getConditions() {
        return conditions;
    }
}
