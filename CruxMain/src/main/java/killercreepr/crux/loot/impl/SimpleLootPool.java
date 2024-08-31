package killercreepr.crux.loot.impl;

import killercreepr.crux.data.Holder;
import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.LootPool;
import killercreepr.crux.loot.LootPoolObject;
import killercreepr.crux.loot.conditions.LootCondition;
import killercreepr.crux.loot.functions.LootFunction;
import killercreepr.crux.util.CruxWeightedSupplier;
import killercreepr.crux.valueproviders.number.NumberProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class SimpleLootPool<T> extends SimpleLootObject<T> implements LootPool<T> {
    private final @NotNull NumberProvider rolls;
    private final @NotNull List<LootPoolObject<T>> data;

    public SimpleLootPool(int weight, float quality,
                          @Nullable List<LootCondition> conditions,
                          @Nullable List<LootFunction<T>> lootFunctions,
                          @NotNull NumberProvider rolls, @NotNull List<LootPoolObject<T>> data) {
        super(weight, quality, conditions, lootFunctions);
        this.rolls = rolls;
        this.data = data;
    }

    public SimpleLootPool(int weight, float quality,
                          @Nullable List<LootFunction<T>> lootFunctions,
                          @NotNull NumberProvider rolls,
                          @NotNull List<LootPoolObject<T>> data) {
        super(weight, quality, lootFunctions);
        this.rolls = rolls;
        this.data = data;
    }

    public SimpleLootPool(int weight, float quality, @NotNull NumberProvider rolls, @NotNull List<LootPoolObject<T>> data) {
        super(weight, quality);
        this.rolls = rolls;
        this.data = data;
    }

    @Override
    public String toString() {
        return "SimpleLootPool{rolls=" + rolls + ", data=" + data + "}";
    }

    public @NotNull List<LootPoolObject<T>> getData() {
        return data;
    }

    public @NotNull NumberProvider getRolls() {
        return rolls;
    }

    public @NotNull Collection<T> populateLoot(@NotNull LootContext context) {
        return populateLoot(context, null, false);
    }

    public @NotNull Collection<T> populateLoot(@NotNull LootContext context, @Nullable Predicate<LootPoolObject<T>> exclude, boolean excludeEmpty) {
        List<T> list = new ArrayList<>();
        List<LootPoolObject<T>> data = exclude == null ? this.data : new ArrayList<>(this.data);
        if(excludeEmpty && exclude != null) data.removeIf(exclude);
        List<LootPoolObject<T>> random = random(data, rolls.sample(context.getRandom()).intValue(), context, exclude);
        for(LootPoolObject<T> x : random){
            Holder<Collection<T>> holder = x.getItems(context);
            Collection<T> items = holder == null ? null : holder.value();
            if(items==null) continue;
            for(T t : items){
                //pool functions first
                for(LootFunction<T> f : getFunctions()){
                    if(f.test(context)) t = f.accept(t, context);
                }
                //then entry functions
                for(LootFunction<T> f : x.getFunctions()){
                    if(f.test(context)) t = f.accept(t, context);
                }
                list.add(t);
            }
            //list.add(i == null ? null : Crux.handlers().item().update(i));
        }
        return list;
    }

    @Override
    public boolean isEmptyWith(@Nullable Predicate<LootPoolObject<T>> exclude){
        List<LootPoolObject<T>> data = exclude == null ? this.data : new ArrayList<>(this.data);
        if(exclude != null) data.removeIf(exclude);
        return data.isEmpty();
    }

    @Override
    public @NotNull List<LootPoolObject<T>> random(@NotNull List<LootPoolObject<T>> data, int rolls, @NotNull LootContext context, @Nullable Predicate<LootPoolObject<T>> exclude){
        List<LootPoolObject<T>> newData;
        if(exclude == null) newData = data;
        else{
            newData = new ArrayList<>(data);
            newData.removeIf(exclude);
        }
        return CruxWeightedSupplier.builder(newData)
            .rolls(rolls)
            .applyContext(context)
            .build().rollList();
    }
}
