package killercreepr.crux.loot;

import killercreepr.crux.loot.api.LootContext;
import killercreepr.crux.loot.api.LootObject;
import killercreepr.crux.loot.api.LootPool;
import killercreepr.crux.loot.api.LootPoolObject;
import killercreepr.crux.loot.api.functions.LootFunction;
import killercreepr.crux.valueproviders.number.NumberProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class SimpleLootPool<T> extends SimpleLootObject<T> implements LootPool<T> {
    private final NumberProvider rolls;
    private final List<LootPoolObject<T>> data = new ArrayList<>();

    public SimpleLootPool(@NotNull NumberProvider rolls, int weight, float quality, @NotNull List<LootPoolObject<T>> data) {
        super(weight, quality);
        this.rolls = rolls;
        this.data.addAll(data);
    }

    public SimpleLootPool(@NotNull NumberProvider rolls, @NotNull LootObject<T> object, @NotNull List<LootPoolObject<T>> data) {
        super(object);
        this.rolls = rolls;
        this.data.addAll(data);
    }

    /*public static @Nullable GrimLootPool deserialize(@NotNull JsonElement element){
        GrimLootObject object = GrimLootObject.deserialize(element);
        if(object == null) return null;
        JsonObject o = element.getAsJsonObject();
        List<ItemPollData> data = new ArrayList<>();
        o.getAsJsonArray("entries").forEach(x ->{
            ItemPollData d = ItemPollData.deserialize(x);
            if(d == null) return;
            data.add(d);
        });

        return new GrimLootPool(IntProvider.deserialize(o.get("rolls")), object, data);
    }

    @Override
    public @NotNull JsonObject serialize() {
        JsonObject o = (JsonObject) super.serialize();
        o.add("rolls", rolls.serialize());
        JsonArray a = new JsonArray();
        for(ItemPollData i : data){
            a.add(i.serialize());
        }
        o.add("entries", a);
        return o;
    }*/

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
        for(LootPoolObject<T> x : random(data, rolls.sample(context.getRandom()).intValue(), context, exclude)){
            T i = x.getItem() == null ? null : x.getItem().value();
            for(LootFunction<T> f : x.getFunctions()){
                if(f.test(context)) i = f.accept(i, context);
            }
            list.add(i);
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
        return SimpleLootTable.random(newData, rolls, context);
    }

    /*public @NotNull List<ItemPollData> random(int rolls, @NotNull LootContext context){
        return random(data, rolls, context, null);
    }*/
}
