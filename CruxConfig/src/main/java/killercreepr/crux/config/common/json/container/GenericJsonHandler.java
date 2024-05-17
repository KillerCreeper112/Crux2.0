package killercreepr.crux.config.common.json.container;

import com.google.gson.JsonElement;
import killercreepr.crux.config.common.json.JsonContext;
import killercreepr.crux.config.common.json.annotation.JsonSerializerID;
import killercreepr.crux.config.common.json.automatic.AutomaticSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GenericJsonHandler<T> implements JsonContainerHandler<Object>, JsonSerializerID {
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
        return new AutomaticSerializer<>(object).serializeToJson(context);
    }

    @Override
    public @Nullable Object deserializeFromJson(@NotNull JsonContext context, @Nullable JsonElement e) {
        return AutomaticSerializer.deserializeFromJson(type, context, e);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return id;
    }
}
