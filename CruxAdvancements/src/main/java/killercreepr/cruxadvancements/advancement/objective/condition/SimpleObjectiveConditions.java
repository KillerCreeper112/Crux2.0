package killercreepr.cruxadvancements.advancement.objective.condition;

import killercreepr.crux.loot.api.LootContext;
import killercreepr.crux.loot.api.conditions.LootCondition;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class SimpleObjectiveConditions implements ObjectiveConditions {
    protected final @NotNull Collection<LootCondition> conditions;

    public SimpleObjectiveConditions(@NotNull Collection<LootCondition> conditions) {
        this.conditions = Collections.unmodifiableCollection(conditions);
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        for(LootCondition c : conditions){
            if(!c.test(ctx)) return false;
        }
        return true;
    }
}
