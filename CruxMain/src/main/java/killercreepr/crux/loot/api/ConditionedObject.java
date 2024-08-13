package killercreepr.crux.loot.api;

import killercreepr.crux.loot.api.conditions.LootCondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ConditionedObject {
    @NotNull
    List<LootCondition> getConditions();
}
