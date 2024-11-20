package killercreepr.cruxadvancements.core.advancement.objective;

import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.cruxadvancements.api.advancement.objective.AdvancementObjective;
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
