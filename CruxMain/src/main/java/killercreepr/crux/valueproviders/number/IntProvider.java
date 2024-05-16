package killercreepr.crux.valueproviders.number;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public abstract class IntProvider implements NumberProvider {
    public abstract @NotNull Number sample(@NotNull Random random);

    public abstract @NotNull Number getMinValue();

    public abstract @NotNull Number getMaxValue();

    public abstract IntProviderType getType();

    //public abstract @NotNull JsonObject serialize();

    public static @Nullable IntProvider deserialize(@NotNull JsonElement element){
        if(!element.isJsonObject()) return null;
        JsonObject o = element.getAsJsonObject();
        IntProviderType type = IntProviderType.valueOf(o.get("type").getAsString().toUpperCase());
        return switch (type){
            case UNIFORM -> UniformInt.deserialize(element);
            case CONSTANT -> ConstantInt.deserialize(element);
        };
    }
}
