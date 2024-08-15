package killercreepr.cruxconfig.config.common.json.handler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import killercreepr.cruxconfig.config.common.json.context.JsonContext;
import killercreepr.cruxconfig.config.common.json.registry.JsonRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@JsonSerializer(id = "map")
public class JsonMapHandler implements JsonObjectHandler<Map<Object, Object>> {
    @Override
    public @NotNull JsonElement serializeToJson(@NotNull JsonContext context, @NotNull Map<Object, Object> object) {
        JsonArray o = new JsonArray();
        JsonRegistry registry = context.getRegistry();
        object.forEach((key, value) ->{
            JsonObject sub = new JsonObject();
            sub.add("key", registry.serializeToJson(key));
            sub.add("value", registry.serializeToJson(value));
            o.add(sub);
        });
        return o;
    }

    @Override
    public @Nullable Map<Object, Object> deserializeFromJson(@NotNull JsonContext context, @Nullable JsonElement element){
        if(!(element instanceof JsonArray a)) return null;
        Map<Object, Object> list = new HashMap<>();
        JsonRegistry registry = context.getRegistry();
        a.forEach(ee ->{
            if(!(ee instanceof JsonObject o)) return;
            Object key = registry.deserializeFromJson(o.get("key"));
            if(key==null) return;
            Object value = registry.deserializeFromJson(o.get("value"));
            if(value==null) return;
            list.put(key, value);
        });
        return list;
    }
}
