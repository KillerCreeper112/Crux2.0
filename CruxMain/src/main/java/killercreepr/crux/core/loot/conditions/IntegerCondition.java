package killercreepr.crux.core.loot.conditions;

import killercreepr.crux.api.loot.LootContext;
import org.jetbrains.annotations.NotNull;

public class IntegerCondition extends BaseCondition {
    protected final int value;
    protected final String operation;

    public IntegerCondition(@NotNull String target, int value, String operation) {
        super(target);
        this.value = value;
        this.operation = operation;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Number num = ctx.info().get(this.target, Number.class);
        if(num == null) return false;
        int actual = num.intValue();

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
