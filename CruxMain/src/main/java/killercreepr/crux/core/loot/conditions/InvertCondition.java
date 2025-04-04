package killercreepr.crux.core.loot.conditions;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import org.jetbrains.annotations.NotNull;

public class InvertCondition extends BaseCondition {
    protected final LootCondition condition;
    public InvertCondition(@NotNull String target, LootCondition condition) {
        super(target);
        this.condition = condition;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        return !condition.test(ctx);
    }
}
