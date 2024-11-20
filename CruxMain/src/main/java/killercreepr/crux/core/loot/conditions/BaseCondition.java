package killercreepr.crux.core.loot.conditions;

import killercreepr.crux.api.loot.conditions.LootCondition;
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
