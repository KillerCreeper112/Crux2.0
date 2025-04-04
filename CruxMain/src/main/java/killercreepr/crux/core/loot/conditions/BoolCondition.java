package killercreepr.crux.core.loot.conditions;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.core.util.CruxString;
import org.jetbrains.annotations.NotNull;

public class BoolCondition extends BaseCondition {
    public BoolCondition(@NotNull String target) {
        super(target);
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Object target = ctx.info().get(this.target);
        return CruxString.parseBoolean(target + "");
    }
}
