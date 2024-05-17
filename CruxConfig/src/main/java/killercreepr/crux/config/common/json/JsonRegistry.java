package killercreepr.crux.config.common.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import killercreepr.crux.config.common.json.annotation.JsonSerializer;
import killercreepr.crux.config.common.json.annotation.JsonSerializerID;
import killercreepr.crux.config.common.json.container.JsonContainerHandler;
import killercreepr.crux.config.common.json.container.JsonListHandler;
import killercreepr.crux.config.common.json.container.JsonMapHandler;
import killercreepr.crux.config.common.json.container.GenericJsonHandler;
import killercreepr.crux.config.common.json.registry.JsonContainerHandlerRegistry;
import killercreepr.crux.valueproviders.number.ConstantNumber;
import killercreepr.crux.valueproviders.number.EquationNumber;
import killercreepr.crux.valueproviders.number.UniformNumber;
import killercreepr.crux.valueproviders.number.UniformNumberArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonRegistry {
    public final String DESERIALIZE_METHOD_NAME = "deserializeFromJson";
    public final Map<String, Class<? extends JsonSerializable>> REGISTRY = new HashMap<>();
    public final JsonContainerHandlerRegistry CONTAINER_REGISTRY = new JsonContainerHandlerRegistry(this);

    public JsonRegistry() {
        registerContainerHandler(List.class, new JsonListHandler());
        registerContainerHandler(Map.class, new JsonMapHandler());
        registerContainerHandler(
                new GenericJsonHandler<>("constant_number", ConstantNumber.class),
                new GenericJsonHandler<>("equation_number", EquationNumber.class),
                new GenericJsonHandler<>("uniform_number", UniformNumber.class),
                new GenericJsonHandler<>("uniform_number_array", UniformNumberArray.class)
        );
    }

    public <T extends JsonSerializable> void register(@NotNull String name, @NotNull Class<T> clazz){
        REGISTRY.put(name, clazz);
    }

    public <T extends JsonContainerHandler<?>> void registerContainerHandler(@NotNull Class<?> clazz, @NotNull T object){
        CONTAINER_REGISTRY.register(clazz, object);
    }

    public <T extends GenericJsonHandler<?>> void registerContainerHandler(@NotNull T... objects){
        for(T object : objects){
            registerContainerHandler(object.getType(), object);
        }
    }

    public void register(@NotNull Class<? extends JsonSerializable>... clazzes){
        for(Class<? extends JsonSerializable> clazz : clazzes){
            register(clazz);
        }
    }

    public @NotNull String getSerializerID(@NotNull Object object){
        if(object instanceof JsonSerializerID i) return i.jsonSerializerID();
        return getSerializeID(object.getClass());
    }

    public <T> @NotNull String getSerializeID(@NotNull Class<T> clazz){
        JsonSerializer serializable = clazz.getAnnotation(JsonSerializer.class);
        if (serializable == null){
            Class<?> superclass = clazz.getSuperclass();
            while(superclass != null){
                serializable = superclass.getAnnotation(JsonSerializer.class);
                if(serializable != null) return serializable.id();
                superclass = superclass.getSuperclass();
            }
            throw new RuntimeException(clazz + " is not annotated with JsonSerializer!");
        }
        return serializable.id();
    }

    public <T extends JsonSerializable> void register(@NotNull Class<T> clazz){
        register(getSerializeID(clazz), clazz);
    }
    public @Nullable Class<? extends JsonSerializable> unregister(@NotNull String name){
        return REGISTRY.remove(name);
    }

    public @Nullable JsonContainerHandler<?> getContainerHandler(@NotNull Class<?> clazz){
        return CONTAINER_REGISTRY.get(clazz);
    }

    public @Nullable Class<? extends JsonSerializable> get(@NotNull String name){
        return REGISTRY.get(name);
    }

    public <T extends JsonSerializable> @Nullable Class<? extends JsonSerializable> get(@NotNull Class<T> from){
        return get(getSerializeID(from));
    }

    public @Nullable JsonPrimitive tryPrimitive(@NotNull Object o){
        return switch (o) {
            case String s -> new JsonPrimitive(s);
            case Number s -> new JsonPrimitive(s);
            case Boolean s -> new JsonPrimitive(s);
            case Character s -> new JsonPrimitive(s);
            default -> null;
        };
    }

    public @Nullable JsonContainerHandler<?> findContainerHandler(@NotNull Class<?> from){
        for(Map.Entry<Class<?>, JsonContainerHandler<?>> entry : CONTAINER_REGISTRY.entrySet()){
            Class<?> clazz = entry.getKey();
            if(clazz.isAssignableFrom(from)){
                return entry.getValue();
            }
        }
        return null;
    }

    public @NotNull JsonElement serializeObject(@NotNull Object object){
        if(object instanceof JsonSerializable s) return serialize(s);
        JsonContainerHandler<?> handler = findContainerHandler(object.getClass());
        if(handler == null){
            JsonElement ele = tryPrimitive(object);
            if(ele == null){
                throw new RuntimeException("Cannot find serialization method for " + object + " (class " + object.getClass().getName() + ")");
            }
            return ele;
        }
        JsonElement element = handler.attemptSerializeToJson(new JsonContext(this), object);
        if(element == null)
            throw new RuntimeException("Object cannot be serialized with " + handler + " (" + object + ")");
        String id = getSerializerID(handler);
        JsonObject o = new JsonObject();
        o.addProperty("id", id);
        o.add("value", element);
        return o;
    }

    public <T extends JsonSerializable> @NotNull JsonElement serialize(@NotNull T object){
        String id = getSerializeID(object.getClass());
        JsonObject o = new JsonObject();
        o.addProperty("id", id);
        o.add("value", object.serializeToJson(new JsonContext(this)));
        return o;
    }

    public @Nullable Object deserialize(@Nullable JsonElement from){
        if(!(from instanceof JsonObject o)){
            if(from instanceof JsonPrimitive pr){
                if(pr.isBoolean()) return pr.getAsBoolean();
                if(pr.isNumber()) return pr.getAsNumber();
                if(pr.isString()) return pr.getAsString();
            }
            return null;
        }
        JsonElement e = o.get("id");
        if(e == null) return null;
        String id = e.getAsString();
        Class<? extends JsonSerializable> clazz = get(id);
        if(clazz == null){
            JsonContainerHandler<?> handler = CONTAINER_REGISTRY.getByName(id);
            return handler == null ? null : handler.deserializeFromJson(new JsonContext(this), o.get("value"));
        }
        JsonElement value = o.get("value");

        Class<?>[] parameterTypes = { JsonElement.class };
        try {
            Method method = clazz.getMethod(DESERIALIZE_METHOD_NAME, parameterTypes);
            if(method.getParameterCount() > 1) return method.invoke(null, new JsonContext(this), value);
            return method.invoke(null, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException x) {
            x.printStackTrace();
        }
        return null;
    }

    public <T> @Nullable T deserialize(@Nullable JsonElement from, @NotNull Class<T> clazz){
        Object o = deserialize(from);
        if(o == null || !clazz.isAssignableFrom(o.getClass())) return null;
        return clazz.cast(o);
    }

}
