package killercreepr.crux.config.common.json.container;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import killercreepr.crux.config.common.json.JsonContext;
import killercreepr.crux.config.common.json.JsonRegistry;
import killercreepr.crux.config.common.json.annotation.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@JsonSerializer(id = "list")
public class JsonListHandler implements JsonContainerHandler<List<Object>> {
    @Override
    public @NotNull JsonElement serializeToJson(@NotNull JsonContext context, @NotNull List<Object> object) {
        JsonRegistry registry = context.getRegistry();
        JsonArray a = new JsonArray();
        for(Object s : object){
            a.add(registry.serializeObject(s));
        }
        return a;
    }

    @Override
    public @Nullable List<Object> deserializeFromJson(@NotNull JsonContext context, @Nullable JsonElement element){
        if(!(element instanceof JsonArray a)) return null;
        JsonRegistry registry = context.getRegistry();
        List<Object> list = new ArrayList<>();
        a.forEach(ee ->{
            Object o = registry.deserialize(ee);
            if(o==null) return;
            list.add(o);
        });
        return list;
    }
}
