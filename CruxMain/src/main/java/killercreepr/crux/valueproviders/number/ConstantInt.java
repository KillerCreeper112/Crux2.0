package killercreepr.crux.valueproviders.number;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ConstantInt extends IntProvider {
    protected final int value;
    public ConstantInt(int value) {
        this.value = value;
    }

    public int getValue() { return this.value; }

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
        return this.value;
    }

    @Override
    public @NotNull IntProviderType getType() {
        return IntProviderType.CONSTANT;
    }

    public static @Nullable ConstantInt deserialize(@NotNull JsonElement e){
        if(!e.isJsonObject()) return null;
        JsonObject o = e.getAsJsonObject();
        return new ConstantInt(o.get("value").getAsInt());
    }

    //todo
    /*@Override
    public @NotNull JsonObject serialize() {
        JsonObject o = new JsonObject();
        o.addProperty("type", getType().toString().toLowerCase());
        o.addProperty("value", value);
        return o;
    }*/

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }
}
