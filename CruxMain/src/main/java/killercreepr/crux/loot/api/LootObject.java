package killercreepr.crux.loot.api;

import killercreepr.crux.loot.api.conditions.LootCondition;
import killercreepr.crux.loot.api.functions.LootFunction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface LootObject<T> extends WeightedObject{
    @NotNull List<LootCondition> getConditions();
    @NotNull List<LootFunction<T>> getFunctions();
}
