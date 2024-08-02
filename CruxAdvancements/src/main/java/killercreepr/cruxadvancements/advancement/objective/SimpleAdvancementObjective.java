package killercreepr.cruxadvancements.advancement.objective;

import org.jetbrains.annotations.NotNull;

public abstract class SimpleAdvancementObjective implements AdvancementObjective {
    protected final @NotNull String criterion;
    public SimpleAdvancementObjective(@NotNull String criterion) {
        this.criterion = criterion;
    }

    @Override
    public @NotNull String getCriterion() {
        return criterion;
    }
}
