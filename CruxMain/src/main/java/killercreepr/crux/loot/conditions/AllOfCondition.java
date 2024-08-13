package killercreepr.crux.loot.conditions;

import killercreepr.crux.loot.api.LootContext;
import killercreepr.crux.loot.api.conditions.LootCondition;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class AllOfCondition implements LootCondition {
    private final Collection<LootCondition> conditions;
    public AllOfCondition(@NotNull Collection<LootCondition> conditions) {
        this.conditions = Collections.unmodifiableCollection(conditions);
    }

    @Override
    public @NotNull String getTarget() {
        return "";
    }

    @Override
    public boolean test(@NotNull LootContext context) {
        for(LootCondition c : conditions){
            if(!c.test(context)) return false;
        }
        return true;
    }
}
