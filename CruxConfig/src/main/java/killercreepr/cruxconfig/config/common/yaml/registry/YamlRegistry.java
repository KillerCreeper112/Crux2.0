package killercreepr.cruxconfig.config.common.yaml.registry;

import killercreepr.crux.util.CruxReflect;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.yaml.automatic.AutoYamlSerializer;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.*;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Represents a registry specifically developed to handle YAML syntax.
 * Keep in mind, this functions differently than Crux's JSON framework.
 * It was made primarily for saving and extracting config values. Therefor meaning that the
 * software knows exactly what classes it needs to serialize and what classes it needs to deserialize.
 */
public class YamlRegistry implements FileRegistry {
    public final YamlObjectHandlerRegistry HANDLER_REGISTRY = new YamlObjectHandlerRegistry(this);

    public void registerHandler(@NotNull AutoYamlSerializer<?>... serializers){
        for(AutoYamlSerializer<?> d : serializers){
            registerHandler(d.getType(), d);
        }
    }

    public <T extends YamlObjectHandler<?>> void registerHandler(@NotNull Class<?> clazz, @NotNull T object){
        HANDLER_REGISTRY.register(clazz, object);
    }

    public @Nullable YamlObjectHandler<?> getContainerHandler(@NotNull Class<?> clazz){
        return HANDLER_REGISTRY.get(clazz);
    }

    public @NotNull Collection<YamlObjectHandler<?>> findPotentialHandlers(@NotNull Class<?> from){
        Collection<YamlObjectHandler<?>> handlers = new HashSet<>();
        for(Map.Entry<Class<?>, YamlObjectHandler<?>> entry : HANDLER_REGISTRY.entrySet()){
            Class<?> clazz = entry.getKey();
            if(from.isAssignableFrom(clazz)){
                handlers.add(entry.getValue());
            }
        }
        return handlers;
    }

    public @Nullable YamlObjectHandler<?> findContainerHandler(@NotNull Class<?> from){
        //first check if there are any exact classes
        Collection<Map.Entry<Class<?>, YamlObjectHandler<?>>> check = new HashSet<>();
        for(Map.Entry<Class<?>, YamlObjectHandler<?>> entry : HANDLER_REGISTRY.entrySet()){
            Class<?> clazz = entry.getKey();
            if(clazz.equals(from)){
                return entry.getValue();
            }
            check.add(entry);
        }

        //then check if any classes are assignable
        for(Map.Entry<Class<?>, YamlObjectHandler<?>> entry : check){
            Class<?> clazz = entry.getKey();
            if(clazz.isAssignableFrom(from)){
                return entry.getValue();
            }
        }
        return null;
    }

    public @Nullable YamlElement serialize(@NotNull Object object){
        YamlObjectHandler<?> handler = findContainerHandler(object.getClass());
        if(handler == null) return null;
        return handler.attemptSerializeToYaml(new YamlContext(this), object);
    }

    public static boolean isSubtypeOfCollection(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class<?> rawClass) {
                return Collection.class.isAssignableFrom(rawClass);
            }
        }
        return false;
    }
    public static boolean isSubtypeOfMap(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class<?> rawClass) {
                return Map.class.isAssignableFrom(rawClass);
            }
        }
        return false;
    }

    /**
     * This should be used
     */
    public @Nullable Object deserializeObject(@NotNull Type type, @NotNull YamlElement from, @NotNull YamlContext context){
        Bukkit.getLogger().severe("DESERLIAIING OBJECT: " + type + " FROM " + from);
        /*if(from != null){
            if(isSubtypeOfCollection(type) && from.isYamlArray()){
                Type[] args = getTypeArguments(type);
                Class<?> rawType = (Class<?>) ((ParameterizedType) type).getRawType();
                return deserializeObjectCollection(rawType, args[0], from.getAsYamlArray());
            }else if(isSubtypeOfMap(type) && from.isYamlObject()){
                Type[] args = getTypeArguments(type);
                Class<?> rawType = (Class<?>) ((ParameterizedType) type).getRawType();
                return deserializeObjectMap(rawType, args[0], args[1], from.getAsYamlObject());
            }
        }
        if(!(type instanceof Class<?> clazz)){
            throw new UnsupportedOperationException(type + " is not a class instance!");
        }*/

        if(isSubtypeOfCollection(type)){
            Type[] args = CruxReflect.getTypeArguments(type);
            Class<?> rawType = (Class<?>) ((ParameterizedType) type).getRawType();
            return deserializeObjectCollection(rawType, args[0], from.getAsYamlArray());
        }else if(isSubtypeOfMap(type)){
            Type[] args = CruxReflect.getTypeArguments(type);
            Class<?> rawType = (Class<?>) ((ParameterizedType) type).getRawType();
            return deserializeObjectMap(rawType, args[0], args[1], from.getAsYamlObject());
        }

        if(!(type instanceof Class<?> clazz)){
            throw new UnsupportedOperationException(type + " is not a class instance!");
        }

        for(YamlObjectHandler<?> handler : findPotentialHandlers(clazz)){
            Object o = handler.deserializeFromYaml(context, from);
            if(o!=null){
                return formatObject(clazz, deserializeObject(o));
            }
        }
        Object object = deserializeObject(from.getAsObject());
        return formatObject(clazz, object);
    }

    public @Nullable Object deserializeObject(@NotNull Type type, @Nullable YamlElement from){
        if(from==null) return null;
        return deserializeObject(type, from, new YamlContext(this));
    }

    public @Nullable Object deserializeObjectCollection(@NotNull Class<?> mapClazz, @NotNull Type firstType,
                                                 @NotNull YamlArray from){
        Object createdMap = CruxReflect.attemptCreation(mapClazz);
        if(createdMap == null) createdMap = new ArrayList<>();

        Collection<Object> map = (Collection<Object> ) createdMap;
        from.forEach((value) ->{
            Object parsedValue = deserializeObject(firstType, value);
            if(parsedValue==null) return;
            map.add(parsedValue);
        });
        return createdMap;
    }

    public @Nullable Object deserializeObjectMap(@NotNull Class<?> mapClazz, @NotNull Type firstType, @NotNull Type secondType,
                                                 @NotNull YamlObject from){
        Object createdMap = CruxReflect.attemptCreation(mapClazz);
        if(createdMap == null) createdMap = new HashMap<>();

        Map<Object, Object> map = (Map<Object, Object>) createdMap;
        from.forEach((key, value) ->{
            Object parsedValue = deserializeObject(secondType, value);
            if(parsedValue == null) return;
            Object parsedKey = key;
            if(firstType == Integer.class){
                parsedKey = Integer.parseInt(key);
            }else if(firstType == Double.class){
                parsedKey = Double.parseDouble(key);
            }else if(firstType == Float.class){
                parsedKey = Float.parseFloat(key);
            }else if(firstType == Short.class){
                parsedKey = Short.parseShort(key);
            }else if(firstType == Long.class){
                parsedKey = Long.parseLong(key);
            }
            map.put(parsedKey, parsedValue);
        });
        return createdMap;
    }

    public @NotNull Object formatObject(@NotNull Class<?> clazz, @NotNull Object object){
        if(clazz.isAssignableFrom(Collection.class) && object instanceof Map<?,?> map){
            return new ArrayList<>(map.values());
        }
        return object;
    }

    public <T> @Nullable T deserialize(@NotNull Class<T> clazz, @NotNull YamlElement from, @NotNull YamlContext context){
        Object object = deserializeObject(clazz, from, context);
        if(object==null) return null;
        if(clazz.isAssignableFrom(object.getClass())) return clazz.cast(object);
        throw new UnsupportedOperationException("Object cannot be cast to " + clazz.getSimpleName() + " (" + object + ")! " + object.getClass().getSimpleName());
    }

    public <T> @Nullable T deserialize(@NotNull Class<T> clazz, @Nullable YamlElement from){
        Object object = deserializeObject(clazz, from);
        if(object==null) return null;
        if(clazz.isAssignableFrom(object.getClass())) return clazz.cast(object);
        throw new UnsupportedOperationException("Object cannot be cast to " + clazz.getSimpleName() + " (" + object + ")! " + object.getClass().getSimpleName());
    }

    public @NotNull Object deserializeObject(@NotNull Object o){
        if(o instanceof Collection<?> l){
            return deserializeCollection(l);
        }
        if(o.getClass().isArray()){
            return deserializeCollection(Arrays.stream(((Object[]) o)).toList());
        }
        if(o instanceof Map<?,?> l) return deserializeMap(l);
        if(o instanceof YamlElement g) return deserializeObject(g.getAsObject());
        return o;
    }

    public @NotNull YamlElement serializeObject(@NotNull Object o){
        if(o instanceof YamlElement d) return d;
        if(o instanceof String s) return new YamlPrimitive(s);
        if(o instanceof Number s) return new YamlPrimitive(s);
        if(o instanceof Boolean s) return new YamlPrimitive(s);
        if(o instanceof Collection<?> l) return serializeCollection(l);
        if(o.getClass().isArray()){
            return serializeCollection(Arrays.stream(((Object[]) o)).toList());
        }
        if(o instanceof Map<?,?> l) return serializeMap(l);
        YamlElement serialized = serialize(o);
        if(serialized != null) return serialized;
        return new YamlGeneric(o);
    }

    @Override
    public @NotNull FileElement serializeToFileElement(@NotNull Object object) {
        return FileElement.fromYaml(serializeObject(object));
    }

    @Override
    public <T> @Nullable T deserialize(@NotNull Class<T> clazz, @Nullable FileElement o) {
        if(o==null) return null;
        return deserialize(clazz, o.toYaml());
    }

    @Override
    public <T> @Nullable T deserialize(@NotNull Class<T> clazz, @Nullable FileElement o, @NotNull FileContext<?> context) {
        if(o==null) return null;
        if(!(context instanceof YamlContext c)) return deserialize(clazz, o);
        return deserialize(clazz, o.toYaml(), c);
    }

    @Override
    public @NotNull Object deserializeObject(@NotNull FileElement o) {
        return deserializeObject(o.toYaml());
    }

    public @NotNull Collection<Object> deserializeCollection(@NotNull Collection<?> list){
        Collection<Object> array = new ArrayList<>(list.size());
        for(Object o : list){
            array.add(deserializeObject(o));
        }
        return array;
    }

    public @NotNull Map<Object, Object> deserializeMap(@NotNull Map<?, ?> list){
        Map<Object, Object> array = new HashMap<>();
        for(Map.Entry<?, ?> entry : list.entrySet()){
            array.put(deserializeObject(entry.getKey()), deserializeObject(entry.getValue()));
        }
        return array;
    }

    public @NotNull YamlArray serializeCollection(@NotNull Collection<?> list){
        YamlArray array = new YamlArray(list.size());
        for(Object o : list){
            array.add(serializeObject(o));
        }
        return array;
    }

    public @NotNull YamlObject serializeMap(@NotNull Map<?, ?> list){
        YamlObject array = new YamlObject();
        for(Map.Entry<?, ?> entry : list.entrySet()){
            array.add(entry.getKey() + "", serializeObject(entry.getValue()));
        }
        return array;
    }
}
