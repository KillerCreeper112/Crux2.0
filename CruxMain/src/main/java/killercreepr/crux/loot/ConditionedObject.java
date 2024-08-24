package killercreepr.crux.loot;

import killercreepr.crux.loot.conditions.LootCondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ConditionedObject {
    @NotNull
    List<LootCondition> getConditions();
}
