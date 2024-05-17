package killercreepr.crux.config.common.json.container;

import com.google.gson.JsonElement;
import killercreepr.crux.config.common.json.JsonContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Requires a public static @Nullable ?Object deserializeFromJson(@Nullable JsonElement from) to be created.
 */
public interface JsonContainerHandler<T> {
    default @Nullable JsonElement attemptSerializeToJson(@NotNull JsonContext context, @NotNull Object object){
        try{
            return serializeToJson(context, (T) object);
        }catch (ClassCastException ignored){ return null; }
    }
    @NotNull JsonElement serializeToJson(@NotNull JsonContext context, @NotNull T object);
    @Nullable T deserializeFromJson(@NotNull JsonContext context, @Nullable JsonElement e);
}
