package killercreepr.crux.loot.conditions;

import killercreepr.crux.loot.api.LootContext;
import killercreepr.crux.loot.api.conditions.LootCondition;
import killercreepr.crux.util.CruxMath;
import org.jetbrains.annotations.NotNull;

public class GrimRandomChanceCondition implements LootCondition {
    protected final float chance;

    public GrimRandomChanceCondition(float chance) {
        this.chance = chance;
    }

    /*@Override
    public @NotNull JsonElement serialize() {
        JsonObject o = new JsonObject();
        o.addProperty("condition", getType().toString().toLowerCase());
        o.addProperty("chance", chance);
        return o;
    }

    public static @Nullable GrimRandomChanceCondition deserialize(@NotNull JsonElement e){
        if(!e.isJsonObject()) return null;
        return new GrimRandomChanceCondition(e.getAsJsonObject().get("chance").getAsFloat());
    }*/

    @Override
    public @NotNull Type getType() {
        return Type.RANDOM_CHANCE;
    }

    @Override
    public boolean test(@NotNull LootContext context) {
        return CruxMath.random(1f, 100f) <= chance;
    }
}
