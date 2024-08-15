package killercreepr.cruxconfig.config.common.json.handler;

import com.google.gson.JsonElement;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializerID;
import killercreepr.cruxconfig.config.common.json.automatic.AutoJsonSerializer;
import killercreepr.cruxconfig.config.common.json.context.JsonContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GenericJsonHandler<T> implements JsonObjectHandler<Object>, JsonSerializerID {
    protected final String id;
    protected final Class<T> type;
    public GenericJsonHandler(String id, Class<T> type) {
        this.id = id;
        this.type = type;
    }

    public Class<T> getType() {
        return type;
    }

    @Override
    public @NotNull JsonElement serializeToJson(@NotNull JsonContext context, @NotNull Object object) {
        return new AutoJsonSerializer<>(object).serializeToJson(context);
    }

    @Override
    public @Nullable Object deserializeFromJson(@NotNull JsonContext context, @Nullable JsonElement e) {
        return AutoJsonSerializer.deserializeFromJson(type, context, e);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return id;
    }
}
