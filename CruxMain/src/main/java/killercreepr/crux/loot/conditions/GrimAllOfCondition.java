package killercreepr.crux.loot.conditions;

import killercreepr.crux.loot.api.LootContext;
import killercreepr.crux.loot.api.conditions.LootCondition;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class GrimAllOfCondition implements LootCondition {
    private final Collection<LootCondition> conditions;

    public GrimAllOfCondition(@NotNull Collection<LootCondition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean test(@NotNull LootContext context) {
        for(LootCondition c : conditions){
            if(!c.test(context)) return false;
        }
        return true;
    }

    /*public static @Nullable GrimAllOfCondition deserialize(@NotNull JsonElement e){
        if(!e.isJsonObject()) return null;
        JsonObject o = e.getAsJsonObject();
        JsonElement term = o.get("terms");
        if(term == null || !term.isJsonArray()) return null;
        Collection<GrimLootCondition> conditions = new ArrayList<>();
        term.getAsJsonArray().forEach(x ->{
            GrimLootCondition c = GrimLootCondition.deserialize(x);
            if(c == null) return;
            conditions.add(c);
        });
        if(conditions.isEmpty()){
            throw new RuntimeException("Conditions cannot be empty!");
        }
        return new GrimAllOfCondition(conditions);
    }

    @Override
    public @NotNull JsonElement serialize() {
        JsonObject o = new JsonObject();
        o.addProperty("condition", getType().toString().toLowerCase());
        JsonArray a = new JsonArray();
        for(GrimLootCondition c : conditions){
            a.add(c.serialize());
        }
        o.add("terms", a);
        return o;
    }*/

    @Override
    public @NotNull Type getType() {
        return Type.ALL_OF;
    }
}
