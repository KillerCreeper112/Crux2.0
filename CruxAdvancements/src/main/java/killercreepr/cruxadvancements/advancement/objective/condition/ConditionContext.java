package killercreepr.cruxadvancements.advancement.objective.condition;

import killercreepr.crux.data.DataExchange;
import org.jetbrains.annotations.NotNull;

public class ConditionContext {
    public static @NotNull ConditionContext simple(@NotNull Object thisObject){
        return new ConditionContext(DataExchange.builder().put("this", thisObject).build());
    }

    protected final @NotNull DataExchange info;
    public ConditionContext(@NotNull DataExchange info) {
        this.info = info;
    }

    public @NotNull DataExchange info() {
        return info;
    }
}
