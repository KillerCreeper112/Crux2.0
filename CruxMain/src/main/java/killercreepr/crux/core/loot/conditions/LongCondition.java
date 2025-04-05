package killercreepr.crux.core.loot.conditions;

import killercreepr.crux.api.loot.LootContext;
import org.jetbrains.annotations.NotNull;

public class LongCondition extends BaseCondition {
    protected final long value;
    protected final String operation;

    public LongCondition(@NotNull String target, long value, String operation) {
        super(target);
        this.value = value;
        this.operation = operation;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Number num = ctx.info().get(this.target, Number.class);
        if(num == null) return false;
        long actual = num.longValue();

        return switch (operation) {
            case "==" -> actual == value;
            case "!=" -> actual != value;
            case ">"  -> actual > value;
            case ">=" -> actual >= value;
            case "<"  -> actual < value;
            case "<=" -> actual <= value;
            default -> throw new IllegalArgumentException("Unknown operation: " + operation);
        };
    }
}
