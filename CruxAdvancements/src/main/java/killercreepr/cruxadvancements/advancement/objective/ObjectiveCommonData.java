package killercreepr.cruxadvancements.advancement.objective;

import killercreepr.crux.loot.conditions.LootCondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ObjectiveCommonData {
    protected final @NotNull String criterion;
    protected final @Nullable LootCondition conditions;

    public ObjectiveCommonData(@NotNull String criterion, @Nullable LootCondition conditions) {
        this.criterion = criterion;
        this.conditions = conditions;
    }

    public @Nullable LootCondition getConditions() {
        return conditions;
    }

    public @NotNull String getCriterion() {
        return criterion;
    }
}
