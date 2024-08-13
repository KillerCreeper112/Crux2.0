package killercreepr.cruxadvancements.advancement.objective;

import killercreepr.crux.loot.api.conditions.LootCondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SimpleAdvancementObjective implements AdvancementObjective {
    protected final @NotNull String criterion;
    protected final @Nullable LootCondition conditions;
    public SimpleAdvancementObjective(@NotNull String criterion, @Nullable LootCondition conditions) {
        this.criterion = criterion;
        this.conditions = conditions;
    }

    @Override
    public @NotNull String getCriterion() {
        return criterion;
    }

    @Override
    public @Nullable LootCondition getConditions() {
        return conditions;
    }
}
