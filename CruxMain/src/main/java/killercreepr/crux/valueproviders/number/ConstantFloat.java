package killercreepr.crux.valueproviders.number;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ConstantFloat extends FloatProvider {
    protected final float value;
    public ConstantFloat(float value) {
        this.value = value;
    }

    public float getValue() { return this.value; }

    @Override
    public @NotNull Number sample(@NotNull Random random) {
        return this.value;
    }

    @Override
    public @NotNull Number getMinValue() {
        return this.value;
    }

    @Override
    public @NotNull Number getMaxValue() {
        return this.value + 1.0F;
    }

    @Override
    public @NotNull FloatProviderType getType() {
        return FloatProviderType.CONSTANT;
    }

    //todo
    /*@Override
    public @NotNull JsonObject serialize() {
        JsonObject o = new JsonObject();
        o.addProperty("type", getType().toString().toLowerCase());
        o.addProperty("value", value);
        return o;
    }*/

    public static @Nullable ConstantFloat deserialize(@NotNull JsonElement e){
        if(!e.isJsonObject()) return null;
        JsonObject o = e.getAsJsonObject();
        return new ConstantFloat(o.get("value").getAsFloat());
    }

    @Override
    public String toString() {
        return Float.toString(this.value);
    }
}
