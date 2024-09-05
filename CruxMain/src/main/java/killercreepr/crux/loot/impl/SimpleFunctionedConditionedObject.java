package killercreepr.crux.loot.impl;

import killercreepr.crux.loot.ConditionedObject;
import killercreepr.crux.loot.FunctionedObject;
import killercreepr.crux.loot.conditions.LootCondition;
import killercreepr.crux.loot.functions.LootFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SimpleFunctionedConditionedObject<T> implements FunctionedObject<T>, ConditionedObject {
    protected final @NotNull List<LootCondition> conditions;
    protected final @NotNull List<LootFunction<T>> functions;

    public SimpleFunctionedConditionedObject(@Nullable List<LootCondition> conditions, @Nullable List<LootFunction<T>> functions) {
        this.conditions = conditions == null ? List.of() : conditions;
        this.functions = functions == null ? List.of() : functions;
    }

    @Override
    public @NotNull List<LootCondition> getConditions() {
        return conditions;
    }

    @Override
    public @NotNull List<LootFunction<T>> getFunctions() {
        return functions;
    }
}
