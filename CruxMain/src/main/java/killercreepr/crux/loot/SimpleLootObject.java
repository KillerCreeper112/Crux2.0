package killercreepr.crux.loot;

import killercreepr.crux.loot.api.LootObject;
import killercreepr.crux.loot.api.conditions.LootCondition;
import killercreepr.crux.loot.api.functions.LootFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SimpleLootObject<T> implements LootObject<T> {
    private final int weight;
    private final float quality;

    private final List<LootCondition> conditions = new ArrayList<>();
    private final List<LootFunction<T>> functions = new ArrayList<>();

    public SimpleLootObject(int weight, float quality) {
        this(weight, quality, null, null);
    }

    public SimpleLootObject(int weight, float quality, @Nullable List<LootCondition> conditions, @Nullable List<LootFunction<T>> functions) {
        this.weight = weight;
        this.quality = quality;
        if(conditions != null) this.conditions.addAll(conditions);
        if(functions != null) this.functions.addAll(functions);
    }

    public SimpleLootObject(@NotNull LootObject<T> object) {
        this.weight = object.getWeight();
        this.quality = object.getQuality();
        conditions.addAll(object.getConditions());
        functions.addAll(object.getFunctions());
    }

    @Override
    public @NotNull List<LootCondition> getConditions() {
        return conditions;
    }

    @Override
    public @NotNull List<LootFunction<T>> getFunctions() {
        return functions;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public float getQuality() {
        return quality;
    }

    /*public static @Nullable GrimLootObject deserialize(@NotNull JsonElement element){
        if(!element.isJsonObject()) return null;
        JsonObject o = element.getAsJsonObject();
        if(o.get("weight") == null) return null;
        List<GrimLootFunction> functions = new ArrayList<>();
        JsonElement e = o.get("functions");
        if(e != null && e.isJsonArray()){
            e.getAsJsonArray().forEach(x ->{
                GrimLootFunction f = GrimLootFunction.deserialize(x);
                if(f != null) functions.add(f);
            });
        }
        List<GrimLootCondition> conditions = new ArrayList<>();
        e = o.get("conditions");
        if(e != null && e.isJsonArray()){
            e.getAsJsonArray().forEach(x ->{
                GrimLootCondition f = GrimLootCondition.deserialize(x);
                if(f != null) conditions.add(f);
            });
        }
        return new GrimLootObject(o.get("weight").getAsInt(), o.get("quality").getAsFloat(), conditions, functions);
    }

    public @NotNull JsonElement serialize() {
        return serialize(new JsonObject());
    }

    protected @NotNull JsonObject serialize(@NotNull JsonObject o){
        o.addProperty("weight", weight);
        o.addProperty("quality", quality);
        JsonArray array = new JsonArray();
        for(GrimLootFunction f : functions){
            array.add(f.serialize());
        }
        if(!array.isEmpty()) o.add("functions", array);

        array = new JsonArray();
        for(GrimLootCondition f : conditions){
            array.add(f.serialize());
        }
        if(!array.isEmpty()) o.add("conditions", array);
        return o;
    }*/
}
