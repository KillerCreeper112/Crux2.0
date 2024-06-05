package killercreepr.cruxconfig.config.common.json;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

/**
 * Requires a public static @Nullable ?Object deserializeFromJson(@Nullable JsonElement from) to be created.
 *
 * You also have the option to create a static function with a JsonContext: deserializeFromJson(@NotNull JsonContext context, @Nullable JsonElement from)
 */
public interface JsonSerializable {
    @NotNull
    JsonElement serializeToJson(@NotNull JsonContext context);
}
