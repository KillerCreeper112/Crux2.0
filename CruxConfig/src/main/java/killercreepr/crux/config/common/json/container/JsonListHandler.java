package killercreepr.crux.config.common.json.container;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import killercreepr.crux.config.common.json.JsonRegistry;
import killercreepr.crux.config.common.json.JsonSerializable;
import killercreepr.crux.config.common.json.annotation.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@JsonSerializer(id = "list")
public class JsonListHandler implements JsonContainerHandler<List<? extends JsonSerializable>> {
    @Override
    public @NotNull JsonElement serializeToJson(@NotNull JsonRegistry registry, @NotNull List<? extends JsonSerializable> object) {
        JsonArray a = new JsonArray();
        for(JsonSerializable s : object){
            a.add(registry.serialize(s));
        }
        return a;
    }

    @Override
    public @Nullable List<? extends JsonSerializable> deserializeFromJson(@NotNull JsonRegistry registry, @Nullable JsonElement element){
        if(!(element instanceof JsonArray a)) return null;
        List<JsonSerializable> list = new ArrayList<>();
        a.forEach(ee ->{
            Object o = registry.deserialize(ee);
            if(o==null) return;
            if(!(o instanceof JsonSerializable s)) return;
            list.add(s);
        });
        return list;
    }
}
