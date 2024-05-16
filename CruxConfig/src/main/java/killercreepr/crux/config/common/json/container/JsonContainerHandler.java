package killercreepr.crux.config.common.json.container;

import com.google.gson.JsonElement;
import killercreepr.crux.config.common.json.JsonRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Requires a public static @Nullable ?Object deserializeFromJson(@Nullable JsonElement from) to be created.
 */
public interface JsonContainerHandler<T> {
    default @Nullable JsonElement attemptSerializeToJson(@NotNull JsonRegistry registry, @NotNull Object object){
        try{
            return serializeToJson(registry, (T) object);
        }catch (ClassCastException ignored){ return null; }
    }
    @NotNull JsonElement serializeToJson(@NotNull JsonRegistry registry, @NotNull T object);
    @Nullable T deserializeFromJson(@NotNull JsonRegistry registry, @Nullable JsonElement e);
}
