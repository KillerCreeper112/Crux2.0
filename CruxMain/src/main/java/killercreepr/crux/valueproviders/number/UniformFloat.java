package killercreepr.crux.valueproviders.number;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class UniformFloat extends FloatProvider {
    protected final float minInclusive;
    protected final float maxInclusive;

    public UniformFloat(float min, float max) {
        this.minInclusive = min;
        this.maxInclusive = max;
    }

    @Override
    public @NotNull Number sample(@NotNull Random random) {
        return random.nextFloat(this.minInclusive, this.maxInclusive +1F);
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
    public FloatProviderType getType() {
        return FloatProviderType.UNIFORM;
    }

    public static @Nullable UniformFloat deserialize(@NotNull JsonElement e){
        if(!e.isJsonObject()) return null;
        JsonObject o = e.getAsJsonObject();
        return new UniformFloat(o.get("min").getAsFloat(), o.get("max").getAsFloat());
    }

    //todo
    /*@Override
    public @NotNull JsonObject serialize() {
        JsonObject o = new JsonObject();
        o.addProperty("type", getType().toString().toLowerCase());
        o.addProperty("min", minInclusive);
        o.addProperty("max", maxExclusive);
        return o;
    }*/

    @Override
    public String toString() {
        return "[" + this.minInclusive + "-" + this.maxInclusive + "]";
    }
}
