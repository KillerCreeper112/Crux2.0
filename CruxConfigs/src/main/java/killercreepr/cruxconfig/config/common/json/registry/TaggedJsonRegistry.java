package killercreepr.cruxconfig.config.common.json.registry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import killercreepr.crux.util.CruxObjects;
import killercreepr.crux.util.CruxReflect;
import killercreepr.crux.valueproviders.number.ConstantNumber;
import killercreepr.crux.valueproviders.number.EquationNumber;
import killercreepr.crux.valueproviders.number.UniformNumber;
import killercreepr.crux.valueproviders.number.UniformNumberArray;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.base.registry.FileObjectHandlerRegistry;
import killercreepr.cruxconfig.config.common.base.registry.FileParsedObjectRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxconfig.config.common.json.JsonSerializable;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializerID;
import killercreepr.cruxconfig.config.common.json.context.JsonContext;
import killercreepr.cruxconfig.config.common.json.handler.GenericJsonHandler;
import killercreepr.cruxconfig.config.common.json.handler.JsonListHandler;
import killercreepr.cruxconfig.config.common.json.handler.JsonMapHandler;
import killercreepr.cruxconfig.config.common.json.handler.JsonObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

public class TaggedJsonRegistry implements FileRegistry, JsonRegistry {
    public final String DESERIALIZE_METHOD_NAME = "deserializeFromJson";
    public final Map<String, Class<? extends JsonSerializable>> REGISTRY = new HashMap<>();
    public final JsonObjectHandlerRegistry OBJECT_HANDLER_REGISTRY = new JsonObjectHandlerRegistry(this);

    public TaggedJsonRegistry() {
        registerJsonHandler(List.class, new JsonListHandler());
        registerJsonHandler(Map.class, new JsonMapHandler());
        registerJsonHandler(
                new GenericJsonHandler<>("constant_number", ConstantNumber.class),
                new GenericJsonHandler<>("equation_number", EquationNumber.class),
                new GenericJsonHandler<>("uniform_number", UniformNumber.class),
                new GenericJsonHandler<>("uniform_number_array", UniformNumberArray.class)
        );
    }

    public <T extends JsonSerializable> void register(@NotNull String name, @NotNull Class<T> clazz){
        REGISTRY.put(name, clazz);
    }

    @Override
    public <T extends FileObjectHandler<?>> void registerFileHandler(@NotNull Class<?> clazz, @NotNull T handler) {
        registerJsonHandler(clazz, (JsonObjectHandler<?>) handler);
    }

    public @NotNull JsonObjectHandlerRegistry getObjectHandlerRegistry(){
        return OBJECT_HANDLER_REGISTRY;
    }

    @Override
    public @NotNull FileObjectHandlerRegistry getHandlerRegistry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull FileParsedObjectRegistry getParsedObjectRegistry() {
        throw new UnsupportedOperationException();
    }

    public  <T extends GenericJsonHandler<?>> void registerJsonHandler(@NotNull T... objects){
        for(T object : objects){
            registerJsonHandler(object.getType(), object);
        }
    }
    public <T extends JsonObjectHandler<?>> void registerJsonHandler(@NotNull Class<?> clazz, @NotNull T object){
        OBJECT_HANDLER_REGISTRY.register(clazz, object);
    }

    @SafeVarargs
    public final void register(@NotNull Class<? extends JsonSerializable>... clazzes){
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

    public @Nullable JsonObjectHandler<?> getContainerHandler(@NotNull Class<?> clazz){
        return OBJECT_HANDLER_REGISTRY.get(clazz);
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

    public @NotNull List<JsonObjectHandler<?>> findPotentialHandlers(@NotNull Class<?> from) {
        List<JsonObjectHandler<?>> handlerList = new ArrayList<>();

        // Collect matching handlers
        for (Map.Entry<Class<?>, JsonObjectHandler<?>> entry : OBJECT_HANDLER_REGISTRY.entrySet()) {
            Class<?> clazz = entry.getKey();
            if (from.isAssignableFrom(clazz) || clazz.isAssignableFrom(from)) {
                handlerList.add(entry.getValue());
            }
        }

        List<Class<?>> fromInheritanceChain = CruxReflect.getClassInheritanceChain(from);

        // Sort the handlers based on their closeness to `from`
        handlerList.sort(Comparator.comparingInt(handler -> CruxReflect.calculateClassCloseness(fromInheritanceChain, handler.getClass())));

        return handlerList;
    }

    public @Nullable JsonObjectHandler<?> findContainerHandler(@NotNull Class<?> from){
        JsonObjectHandler<?> handler = OBJECT_HANDLER_REGISTRY.get(from);
        if(handler != null) return handler;
        List<JsonObjectHandler<?>> list = findPotentialHandlers(from);
        return list.isEmpty() ? null : list.getFirst();

        /*for(Map.Entry<Class<?>, JsonObjectHandler<?>> entry : OBJECT_HANDLER_REGISTRY.entrySet()){
            Class<?> clazz = entry.getKey();
            if(clazz.isAssignableFrom(from)){
                return entry.getValue();
            }
        }
        return null;*/
    }

    public @NotNull JsonElement rawSerializeObject(@NotNull Object object){
        if(object instanceof JsonSerializable s) return serialize(s);
        JsonPrimitive ele = tryPrimitive(object);
        if(ele != null) return ele;

        JsonObjectHandler<?> handler = findContainerHandler(object.getClass());
        if(handler == null){
            throw new RuntimeException("Cannot find serialization method for " + object + " (class " + object.getClass().getName() + ")");
        }
        JsonElement element = handler.attemptSerializeToJson(new JsonContext(this), object);
        if(element == null)
            throw new RuntimeException("Object cannot be serialized with " + handler + " (" + object + ")");
        return element;
    }

    @Override
    public @NotNull JsonElement serializeToJson(@NotNull Object object){
        if(object instanceof JsonSerializable s) return serialize(s);
        JsonElement ele = tryPrimitive(object);
        if(ele != null) return ele;

        JsonObjectHandler<?> handler = findContainerHandler(object.getClass());
        if(handler == null){
            throw new RuntimeException("Cannot find serialization method for " + object + " (class " + object.getClass().getName() + ")");
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

    @Override
    public @Nullable Object deserializeFromJson(@Nullable JsonElement from){
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
            JsonObjectHandler<?> handler = OBJECT_HANDLER_REGISTRY.getByName(id);
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

    public <T> @Nullable T deserializeFromJson(@NotNull Class<T> clazz, @Nullable JsonElement from){
        Object o = deserializeFromJson(from);
        if(o==null) return null;
        return CruxObjects.attemptCast(clazz, o);
        /*if(o == null || !clazz.isAssignableFrom(o.getClass())) return null;
        return clazz.cast(o);*/
    }

    @Override
    public <T> @Nullable T deserializeFromJson(@NotNull Class<T> clazz, @Nullable JsonElement o, @NotNull JsonContext context) {
        return deserializeFromJson(clazz, o);//todo make context actually used
    }

    @Override
    public @Nullable Object deserializeObjectFromJson(@NotNull JsonElement o) {
        return deserializeFromJson(o);
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull Object object) {
        return FileElement.fromJson(serializeToJson(object));
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull Object object, @NotNull FileContext<?> context) {
        return serializeToFile(object);//todo make context actually used
    }

    @Override
    public <T> @Nullable T deserializeFromFile(@NotNull Type type, @Nullable FileElement o) {
        if(o == null) return null;
        return deserializeFromJson(type, o.toJson());
    }

    @Override
    public <T> @Nullable T deserializeFromFile(@NotNull Type type, @Nullable FileElement o, @NotNull FileContext<?> context) {
        if(o == null) return null;
        return deserializeFromJson(type, o.toJson(), (JsonContext) context);
    }

    @Override
    public <T> @Nullable T deserializeFromJson(@NotNull Type type, @Nullable JsonElement o) {
        if(o == null) return null;
        Object object = deserializeFromJson(o);
        return (T) object;
    }

    //todo actually make context used
    @Override
    public <T> @Nullable T deserializeFromJson(@NotNull Type type, @Nullable JsonElement o, @NotNull JsonContext context) {
        if(o == null) return null;
        Object object = deserializeFromJson(o);
        return (T) object;
    }

    @Override
    public <T> @Nullable T deserializeFromFile(@NotNull Class<T> clazz, @Nullable FileElement o) {
        if(o==null) return null;
        return deserializeFromJson(clazz, o.toJson());
    }

    @Override
    public <T> @Nullable T deserializeFromFile(@NotNull Class<T> clazz, @Nullable FileElement o, @NotNull FileContext<?> context) {
        return deserializeFromFile(clazz, o);
    }

    @Override
    public @Nullable Object deserializeObjectFromFile(@NotNull FileElement o) {
        return deserializeFromJson(o.toJson());
    }
}
