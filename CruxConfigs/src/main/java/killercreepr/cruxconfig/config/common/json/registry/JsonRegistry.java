package killercreepr.cruxconfig.config.common.json.registry;

import com.google.gson.JsonElement;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.json.context.JsonContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public interface JsonRegistry extends FileRegistry {
    /*default <T extends GenericJsonHandler<?>> void registerJsonHandler(@NotNull T... objects){
        for(T object : objects){
            registerJsonHandler(object.getType(), object);
        }
    }*/
    @NotNull JsonElement serializeToJson(@NotNull Object object);
    @Nullable Object deserializeFromJson(@Nullable JsonElement o);
    <T> @Nullable T deserializeFromJson(@NotNull Type type, @Nullable JsonElement o);
    <T> @Nullable T deserializeFromJson(@NotNull Type type, @Nullable JsonElement o, @NotNull JsonContext context);
    <T> @Nullable T deserializeFromJson(@NotNull Class<T> clazz, @Nullable JsonElement o);
    <T> @Nullable T deserializeFromJson(@NotNull Class<T> clazz, @Nullable JsonElement o, @NotNull JsonContext context);
    @Nullable Object deserializeObjectFromJson(@NotNull JsonElement o);
    //<T extends JsonObjectHandler<?>> void registerJsonHandler(@NotNull Class<?> clazz, @NotNull T handler);
}
