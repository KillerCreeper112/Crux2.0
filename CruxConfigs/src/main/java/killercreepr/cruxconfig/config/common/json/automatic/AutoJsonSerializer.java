package killercreepr.cruxconfig.config.common.json.automatic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import killercreepr.crux.util.CruxReflect;
import killercreepr.cruxconfig.config.common.json.JsonContext;
import killercreepr.cruxconfig.config.common.json.JsonRegistry;
import killercreepr.cruxconfig.config.common.json.JsonSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

public class AutoJsonSerializer<T> implements JsonSerializable {
    protected final @NotNull T object;
    public AutoJsonSerializer(@NotNull T object) {
        this.object = object;
    }

    @Override
    public @NotNull JsonElement serializeToJson(@NotNull JsonContext context) {
        JsonObject o = new JsonObject();
        JsonRegistry registry = context.getRegistry();
        for(Field field : CruxReflect.getNonStaticDeclaredFields(object.getClass())){
            if(Modifier.isStatic(field.getModifiers())) continue;
            try{
                boolean x = field.canAccess(object);
                field.setAccessible(true);
                Object obj = field.get(object);
                field.setAccessible(x);
                if(obj == null) continue;
                JsonElement serialized = registry.serializeObject(obj);
                o.add(field.getName(), serialized);
            }catch (IllegalAccessException ignored){}
        }
        return o;
    }

    public static <T> @Nullable T deserializeFromJson(@NotNull Class<T> clazz, @NotNull JsonContext context, @Nullable JsonElement ele){
        if(!(ele instanceof JsonObject o)) return null;
        JsonRegistry registry = context.getRegistry();
        Map<String, Object> fields = new LinkedHashMap<>();
        Map<String, JsonElement> jsonMap = o.asMap();
        for(Field field : CruxReflect.getNonStaticDeclaredFields(clazz)){
            Object found  = registry.deserialize(jsonMap.get(field.getName()));
            fields.put(field.getName(), found);
        }
        return CruxReflect.attemptCreation(clazz, fields);
    }
}
