package killercreepr.crux.api.loot;

import killercreepr.crux.api.loot.conditions.LootCondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ConditionedObject {
    @NotNull
    List<LootCondition> getConditions();
    default boolean testConditions(@NotNull LootContext ctx){
        for(LootCondition condition : getConditions()){
            if(!condition.test(ctx)) return false;
        }
        return true;
    }
}
