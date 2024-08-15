package killercreepr.cruxconfig.config.common.json;

import com.google.gson.JsonElement;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.json.container.GenericJsonHandler;
import killercreepr.cruxconfig.config.common.json.container.JsonContainerHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public interface JsonRegistry extends FileRegistry {
    default <T extends GenericJsonHandler<?>> void registerHandler(@NotNull T... objects){
        for(T object : objects){
            registerHandler(object.getType(), object);
        }
    }
    @NotNull JsonElement serializeObject(@NotNull Object object);
    @Nullable Object deserialize(@Nullable JsonElement o);
    <T> @Nullable T deserialize(@NotNull Type type, @Nullable JsonElement o);
    <T> @Nullable T deserialize(@NotNull Class<T> clazz, @Nullable JsonElement o);
    <T> @Nullable T deserialize(@NotNull Class<T> clazz, @Nullable JsonElement o, @NotNull JsonContext context);
    @Nullable Object deserializeObject(@NotNull JsonElement o);
    <T extends JsonContainerHandler<?>> void registerHandler(@NotNull Class<?> clazz, @NotNull T handler);
}
