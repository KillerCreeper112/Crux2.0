package killercreepr.crux.loot.conditions;

import killercreepr.crux.loot.api.LootContext;
import killercreepr.crux.util.CruxMath;
import org.jetbrains.annotations.NotNull;

public class GrimRandomLuckChanceCondition extends GrimRandomChanceCondition{
    private final float luckMultiplier;

    public GrimRandomLuckChanceCondition(float chance, float luckMultiplier) {
        super(chance);
        this.luckMultiplier = luckMultiplier;
    }

    /*@Override
    public @NotNull JsonElement serialize() {
        JsonObject o = new JsonObject();
        o.addProperty("condition", getType().toString().toLowerCase());
        o.addProperty("chance", chance);
        o.addProperty("luck_multiplier", luckMultiplier);
        return o;
    }

    public static @Nullable GrimRandomLuckChanceCondition deserialize(@NotNull JsonElement e){
        if(!e.isJsonObject()) return null;
        JsonElement m = e.getAsJsonObject().get("luck_multiplier");
        return new GrimRandomLuckChanceCondition(e.getAsJsonObject().get("chance").getAsFloat(), m == null ? 1f : m.getAsFloat());
    }*/

    @Override
    public @NotNull Type getType() {
        return Type.LUCK_CHANCE;
    }

    @Override
    public boolean test(@NotNull LootContext context) {
        float c = (chance + (context.getLuck() * luckMultiplier));
        return CruxMath.random(1f, 100f) <= c;
    }
}
