package killercreepr.crux.loot.impl.conditions;

import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.conditions.LootCondition;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class AnyOfCondition implements LootCondition {
    private final Collection<LootCondition> conditions;

    public AnyOfCondition(@NotNull Collection<LootCondition> conditions) {
        this.conditions = Collections.unmodifiableCollection(conditions);
    }

    @Override
    public @NotNull String getTarget() {
        return "";
    }

    @Override
    public boolean test(@NotNull LootContext context) {
        for(LootCondition c : conditions){
            if(c.test(context)) return true;
        }
        return false;
    }
}
