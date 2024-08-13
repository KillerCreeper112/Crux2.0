package killercreepr.crux.loot.conditions;

import killercreepr.crux.loot.api.conditions.LootCondition;
import org.jetbrains.annotations.NotNull;

public abstract class BaseCondition implements LootCondition {
    protected final @NotNull String target;
    protected BaseCondition(@NotNull String target) {
        this.target = target;
    }

    @Override
    public @NotNull String getTarget() {
        return target;
    }
}
