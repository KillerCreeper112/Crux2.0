package killercreepr.crux.loot.impl;

import killercreepr.crux.loot.LootConditionedObject;
import killercreepr.crux.loot.conditions.LootCondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class SimpleConditionedObject extends SimpleWeighted implements LootConditionedObject {
    private final @NotNull List<LootCondition> conditions;

    public SimpleConditionedObject(int weight, float quality, @Nullable List<LootCondition> conditions) {
        super(weight, quality);
        this.conditions = conditions == null ? List.of() : Collections.unmodifiableList(conditions);
    }

    public SimpleConditionedObject(int weight, float quality){
        this(weight, quality, null);
    }

    @Override
    public @NotNull List<LootCondition> getConditions() {
        return conditions;
    }
}
