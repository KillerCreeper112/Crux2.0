package killercreepr.crux.valueproviders.number;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FloatProvider implements NumberProvider {
    public abstract FloatProviderType getType();
    public static @Nullable FloatProvider deserialize(@NotNull JsonElement element){
        if(!element.isJsonObject()) return null;
        JsonObject o = element.getAsJsonObject();
        FloatProviderType type = FloatProviderType.valueOf(o.get("type").getAsString().toUpperCase());
        return switch (type){
            case UNIFORM -> UniformFloat.deserialize(element);
            case CONSTANT -> ConstantFloat.deserialize(element);
        };
    }
}
