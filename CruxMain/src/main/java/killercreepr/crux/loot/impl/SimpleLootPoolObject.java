package killercreepr.crux.loot.impl;

import killercreepr.crux.data.Holder;
import killercreepr.crux.loot.LootPoolObject;
import killercreepr.crux.loot.conditions.LootCondition;
import killercreepr.crux.loot.functions.LootFunction;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class SimpleLootPoolObject<T> extends SimpleLootObject<T> implements LootPoolObject<T> {
    protected final @Nullable Holder<Collection<T>> item;

    public SimpleLootPoolObject(int weight, float quality, @Nullable List<LootCondition> conditions, @Nullable List<LootFunction<T>> lootFunctions, @Nullable Holder<Collection<T>> item) {
        super(weight, quality, conditions, lootFunctions);
        this.item = item;
    }

    public SimpleLootPoolObject(int weight, float quality, @Nullable List<LootFunction<T>> lootFunctions, @Nullable Holder<Collection<T>> item) {
        super(weight, quality, lootFunctions);
        this.item = item;
    }

    public SimpleLootPoolObject(int weight, float quality, @Nullable Holder<Collection<T>> item) {
        super(weight, quality);
        this.item = item;
    }

    @Override
    public String toString() {
        return "SimpleLootPoolObject{weight=" + weight + ", quality=" + quality + ", item=" + item + ", item_value=" + (item==null?"null":item.value()) + ", conditions=" + getConditions() + ", functions=" + getFunctions() + "}";
    }

    @Override
    public @Nullable Holder<Collection<T>> getItems() {
        return item;
    }
}
