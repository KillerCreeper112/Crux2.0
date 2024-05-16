package killercreepr.crux.valueproviders.number;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class UniformInt extends IntProvider {
    private final int minInclusive;
    private final int maxInclusive;

    public UniformInt(int min, int max) {
        this.minInclusive = min;
        this.maxInclusive = max;
    }

    @Override
    public @NotNull Number sample(@NotNull Random random) {
        return random.nextInt(this.minInclusive, this.maxInclusive +1);
    }

    @Override
    public @NotNull Number getMinValue() {
        return this.minInclusive;
    }

    @Override
    public @NotNull Number getMaxValue() {
        return this.maxInclusive;
    }

    @Override
    public IntProviderType getType() {
        return IntProviderType.UNIFORM;
    }

    public static @Nullable UniformInt deserialize(@NotNull JsonElement e){
        if(!e.isJsonObject()) return null;
        JsonObject o = e.getAsJsonObject();
        return new UniformInt(o.get("min").getAsInt(), o.get("max").getAsInt());
    }

    //todo
    /*@Override
    public @NotNull JsonObject serialize() {
        JsonObject o = new JsonObject();
        o.addProperty("type", getType().toString().toLowerCase());
        o.addProperty("min", minInclusive);
        o.addProperty("max", maxInclusive);
        return o;
    }*/

    @Override
    public String toString() {
        return "[" + this.minInclusive + "-" + this.maxInclusive + "]";
    }
}
