package killercreepr.cruxconfig.config.common.json.registry;

import com.google.gson.JsonElement;
import killercreepr.cruxconfig.config.common.base.BaseFileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.json.context.JsonContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public class SimpleJsonRegistry extends BaseFileRegistry implements JsonRegistry {
    @Override
    public @NotNull JsonElement serializeToJson(@Nullable Object object) {
        return serializeToFile(object).toJson();
    }

    @Override
    public @Nullable Object deserializeFromJson(@Nullable JsonElement o) {
        if(o==null) return null;
        return deserializeObjectFromFile(FileElement.fromJson(o));
    }

    @Override
    public <T> @Nullable T deserializeFromJson(@NotNull Type type, @Nullable JsonElement o) {
        if(o==null) return null;
        return deserializeFromFile(type, FileElement.fromJson(o));
    }

    @Override
    public <T> @Nullable T deserializeFromJson(@NotNull Type type, @Nullable JsonElement o, @NotNull JsonContext context) {
        if(o==null) return null;
        return deserializeFromFile(type, FileElement.fromJson(o), context);
    }

    @Override
    public <T> @Nullable T deserializeFromJson(@NotNull Class<T> clazz, @Nullable JsonElement o) {
        if(o==null) return null;
        return deserializeFromFile(clazz, FileElement.fromJson(o));
    }

    @Override
    public <T> @Nullable T deserializeFromJson(@NotNull Class<T> clazz, @Nullable JsonElement o, @NotNull JsonContext context) {
        if(o==null) return null;
        return deserializeFromFile(clazz, FileElement.fromJson(o), context);
    }

    @Override
    public @Nullable Object deserializeObjectFromJson(@NotNull JsonElement o) {
        return deserializeObjectFromFile(FileElement.fromJson(o));
    }
}
