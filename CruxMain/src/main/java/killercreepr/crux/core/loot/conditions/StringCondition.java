package killercreepr.crux.core.loot.conditions;

import killercreepr.crux.api.loot.LootContext;
import org.jetbrains.annotations.NotNull;

public class StringCondition extends BaseCondition {
    protected final String match;
    protected final boolean ignoreCase;
    public StringCondition(@NotNull String target, String match, boolean ignoreCase) {
        super(target);
        this.match = match;
        this.ignoreCase = ignoreCase;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Object target = ctx.info().get(this.target);
        if(ignoreCase){
            if(!match.equalsIgnoreCase(target + "")) return false;
        }else if(!match.equals(target + "")) return false;
        return true;
    }
}
