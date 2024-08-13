package killercreepr.crux.loot;

import killercreepr.crux.loot.api.LootObject;
import killercreepr.crux.loot.api.conditions.LootCondition;
import killercreepr.crux.loot.api.functions.LootFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class SimpleLootObject<T> extends SimpleConditionedObject implements LootObject<T> {
    private final @NotNull List<LootFunction<T>> functions;

    public SimpleLootObject(int weight, float quality, @Nullable List<LootCondition> conditions, @Nullable List<LootFunction<T>> functions) {
        super(weight, quality, conditions);
        this.functions = functions == null ? List.of() : Collections.unmodifiableList(functions);
    }

    public SimpleLootObject(int weight, float quality, @Nullable List<LootFunction<T>> functions) {
        this(weight, quality, null, functions);
    }

    public SimpleLootObject(int weight, float quality){
        this(weight, quality, null);
    }


    @Override
    public @NotNull List<LootFunction<T>> getFunctions() {
        return functions;
    }
}
