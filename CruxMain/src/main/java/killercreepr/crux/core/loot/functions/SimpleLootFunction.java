package killercreepr.crux.core.loot.functions;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.loot.functions.LootFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public class SimpleLootFunction<T> implements LootFunction<T> {
    protected final Collection<LootCondition> conditions = new ArrayList<>();
    public SimpleLootFunction(@Nullable Collection<LootCondition> conditions) {
        if(conditions != null) this.conditions.addAll(conditions);
    }

    @Override
    public T accept(@Nullable T i, @NotNull LootContext context){
        return i;
    }

    @Override
    public boolean test(@NotNull LootContext context){
        for(LootCondition c : conditions){
            if(!c.test(context)) return false;
        }
        return true;
    }

    /*public @NotNull JsonElement serialize() { return new JsonObject(); }

    public @Nullable JsonArray serializeConditions(){
        if(conditions.isEmpty()) return null;
        JsonArray a = new JsonArray();
        for(GrimLootCondition c : conditions){
            a.add(c.serialize());
        }
        return a;
    }

    public static @Nullable GrimLootFunction deserialize(@NotNull JsonElement element){
        if(!element.isJsonObject()) return null;
        JsonObject o = element.getAsJsonObject();
        JsonElement f = o.get("function");
        if(f == null) return null;
        Type type = Type.valueOf(f.getAsString().toUpperCase());
        switch (type){
            case RANDOM_ATTRIBUTES -> {
                return GrimAttributeFunction.deserialize(element);
            }
            case RANDOM_ENCHANTS -> {
                return GrimEnchantFunction.deserialize(element);
            }
        }
        return null;
    }

    protected static @NotNull Collection<GrimLootCondition> deserializeConditions(@NotNull JsonObject o){
        Collection<GrimLootCondition> list = new ArrayList<>();
        JsonElement con = o.get("conditions");
        if(con != null && con.isJsonArray()){
            con.getAsJsonArray().forEach(x ->{
                GrimLootCondition c = GrimLootCondition.deserialize(x);
                if(c != null) list.add(c);
            });
        }
        return list;
    }*/
}
