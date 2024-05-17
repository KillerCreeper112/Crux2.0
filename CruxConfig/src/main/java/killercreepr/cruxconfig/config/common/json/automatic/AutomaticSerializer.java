package killercreepr.cruxconfig.config.common.json.automatic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import killercreepr.cruxconfig.config.common.json.JsonContext;
import killercreepr.cruxconfig.config.common.json.JsonRegistry;
import killercreepr.cruxconfig.config.common.json.JsonSerializable;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

public class AutomaticSerializer<T> implements JsonSerializable {
    protected final @NotNull T object;
    public AutomaticSerializer(@NotNull T object) {
        this.object = object;
    }

    @Override
    public @NotNull JsonElement serializeToJson(@NotNull JsonContext context) {
        JsonObject o = new JsonObject();
        JsonRegistry registry = context.getRegistry();
        for(Field field : object.getClass().getDeclaredFields()){
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
        for(Field field : clazz.getDeclaredFields()){
            Object found  = registry.deserialize(jsonMap.get(field.getName()));
            fields.put(field.getName(), found);
            Bukkit.getLogger().log(Level.WARNING, "FIELD - " + field.getName());
        }
        Object[] fieldsArray = fields.values().toArray(new Object[0]);
        Bukkit.getLogger().log(Level.WARNING, fieldsArray.length + " LEN + " + fields.keySet());

        for(Constructor<?> constructor : clazz.getConstructors()){
            try{
                T object = (T) constructor.newInstance();
                fields.forEach((key, value) ->{
                    try {
                        Field field = object.getClass().getField(key);
                        boolean x = field.canAccess(object);
                        field.setAccessible(true);
                        field.set(object, value);
                        field.setAccessible(x);
                    } catch (NoSuchFieldException | IllegalAccessException ignored) {}
                });
                return object;
            }catch (InvocationTargetException | InstantiationException |
                    IllegalAccessException | IllegalArgumentException ignored){
                try{
                    T object = (T) constructor.newInstance(fieldsArray);
                    return object;
                }catch (InvocationTargetException | InstantiationException |
                        IllegalAccessException | IllegalArgumentException ignoredAgain){
                }
            }
        }
        return null;
    }
}
