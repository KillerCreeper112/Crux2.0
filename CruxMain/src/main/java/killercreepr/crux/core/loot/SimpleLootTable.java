package killercreepr.crux.core.loot;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.LootPool;
import killercreepr.crux.api.loot.LootPoolObject;
import killercreepr.crux.api.loot.LootTable;
import killercreepr.crux.api.loot.opened.OpenedLootObject;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class SimpleLootTable<T> implements LootTable.Accessible<T>, OpenedLootObject<T> {
    private final Key key;
    private final NumberProvider rolls;
    private final List<LootPool<T>> pools;
    public SimpleLootTable(@NotNull Key key, @NotNull NumberProvider rolls, @NotNull List<LootPool<T>> pools) {
        this.key = key;
        this.rolls = rolls;
        this.pools = pools;
    }

    @Override
    public String toString() {
        return "SimpleLootTable{key=" + key + ", rolls=" + rolls + ", pools=" + pools + "}";
    }

    public @NotNull NumberProvider getRolls() {
        return rolls;
    }

    public @NotNull List<LootPool<T>> getPools() {
        return pools;
    }

    @Override
    public @NotNull List<T> populateLoot(@NotNull LootContext context) {
        return populateLoot(context, null, false);
    }

    @Override
    public @NotNull List<T> populateLoot(@NotNull LootContext context, @Nullable Predicate<LootPoolObject<T>> exclude, boolean excludeEmpty) {
        return populateLoot(context, exclude, excludeEmpty, null);
    }

    @Override
    public @NotNull List<T> populateLoot(@NotNull LootContext context, @Nullable Predicate<LootPoolObject<T>> exclude,
                                         boolean excludeEmpty, @Nullable Function<LootPool<T>, Collection<T>> poolFunction) {
        List<T> list = new ArrayList<>();
        List<LootPool<T>> data = exclude == null ? this.pools : new ArrayList<>(this.pools);
        if(excludeEmpty && exclude != null) data.removeIf(x -> x.isEmptyWith(exclude));
        for(LootPool<T> pool : getPools()){
            if(poolFunction != null){
                Collection<T> parsed = poolFunction.apply(pool);
                if(parsed == null || parsed.isEmpty()) continue;
                list.addAll(parsed);
                continue;
            }
            list.addAll(pool.populateLoot(context));
        }
        return list;
    }

    /*@Override
    public @NotNull List<LootPool<T>> random(int rolls, @NotNull LootContext context){
        return CruxWeightedSupplier.builder(pools)
            .applyContext(context)
            .rolls(rolls)
            .build().rollList();
    }*/

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public @NotNull Collection<LootPoolObject<T>> getAllItems() {
        Collection<LootPoolObject<T>> list = new ArrayList<>();
        pools.forEach(pool ->{
            if(!(pool instanceof OpenedLootObject<?> l)) return;
            list.addAll((Collection<? extends LootPoolObject<T>>) (Collection) l.getAllItems());
        });
        return list;
    }
}
