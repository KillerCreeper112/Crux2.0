package killercreepr.cruxadvancements.advancement.objective;

import killercreepr.crux.loot.conditions.LootCondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SimpleAdvancementObjective implements AdvancementObjective {
    protected final @NotNull ObjectiveCommonData data;
    public SimpleAdvancementObjective(@NotNull ObjectiveCommonData data) {
        this.data = data;
    }
    @Override
    public @NotNull String getCriterion() {
        return data.getCriterion();
    }

    @Override
    public @Nullable LootCondition getConditions() {
        return data.getConditions();
    }
}
