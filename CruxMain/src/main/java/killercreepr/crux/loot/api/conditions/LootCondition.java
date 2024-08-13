package killercreepr.crux.loot.api.conditions;

import killercreepr.crux.loot.api.LootContext;
import org.jetbrains.annotations.NotNull;

public interface LootCondition {
    boolean test(@NotNull LootContext context);

    /*public @NotNull JsonElement serialize() { return new JsonObject(); }

    public static @Nullable GrimLootCondition deserialize(@NotNull JsonElement element){
        if(!element.isJsonObject()) return null;
        JsonObject o = element.getAsJsonObject();
        JsonElement f = o.get("condition");
        if(f == null) return null;
        Type type = Type.valueOf(f.getAsString().toUpperCase());
        switch (type){
            case RANDOM_CHANCE -> {
                return GrimRandomChanceCondition.deserialize(element);
            }
            case ANY_OF ->{
                return GrimAnyOfCondition.deserialize(element);
            }
            case ALL_OF ->{ return GrimAllOfCondition.deserialize(element); }
            case LUCK_CHANCE -> { return GrimRandomLuckChanceCondition.deserialize(element); }
        }
        return null;
    }*/

    default @NotNull Type getType(){ return null; }

    public enum Type{
        RANDOM_CHANCE,
        ANY_OF,
        ALL_OF,
        LUCK_CHANCE
    }
}
