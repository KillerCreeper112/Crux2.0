package killercreepr.cruxconfig.config.common.yaml.registry;

import killercreepr.crux.util.CruxReflect;
import killercreepr.crux.valueproviders.number.ConstantNumber;
import killercreepr.crux.valueproviders.number.EquationNumber;
import killercreepr.crux.valueproviders.number.UniformNumber;
import killercreepr.cruxconfig.config.common.yaml.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.automatic.AutoYamlSerializer;
import killercreepr.cruxconfig.config.common.yaml.element.*;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class YamlRegistry {
    public final YamlObjectHandlerRegistry HANDLER_REGISTRY = new YamlObjectHandlerRegistry(this);

    public YamlRegistry() {
        registerHandler(
                AutoYamlSerializer.notNull(ConstantNumber.class),
                AutoYamlSerializer.notNull(EquationNumber.class),
                AutoYamlSerializer.notNull(UniformNumber.class)
        );
        //registerHandler(NumberProvider.class, new NumTest());
    }

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

    public @Nullable Object deserializeObject(@NotNull Class<?> clazz, @Nullable YamlElement from){
        for(YamlObjectHandler<?> handler : findPotentialHandlers(clazz)){
            Object o = handler.deserializeFromYaml(new YamlContext(this), from);
            if(o!=null){
                Object object = deserializeObject(o);
                return convertObject(clazz, object);
            }
        }
        return from==null?null: convertObject(clazz, deserializeObject(from.getAsObject()));
    }

    public @NotNull Object convertObject(@NotNull Class<?> clazz, @NotNull Object object){
        if(object instanceof Map<?,?> map && Map.class.isAssignableFrom(clazz)){
            return convertMap(clazz, map);
        }
        return object;
    }

    public @NotNull Map<?, ?> convertMap(@NotNull Class<?> type, @NotNull Map<?, ?> map){
        Class<?> first = CruxReflect.getFirstMapClass((Class<? extends Map<?,?>>) type);
        Map<Object, Object> newMap = CruxReflect.attemptCreation(map.getClass());
        if(Double.class.isAssignableFrom(first)){
            computeMap(newMap, map, key -> Double.parseDouble((String) key));
        }else if(Float.class.isAssignableFrom(first)){
            computeMap(newMap, map, key -> Float.parseFloat((String) key));
        }else if(Long.class.isAssignableFrom(first)){
            computeMap(newMap, map, key -> Long.parseLong((String) key));
        }else if(Integer.class.isAssignableFrom(first)){
            computeMap(newMap, map, key -> Integer.parseInt((String) key));
        }else if(Short.class.isAssignableFrom(first)){
            computeMap(newMap, map, key -> Short.parseShort((String) key));
        }
        return newMap;
    }

    public @NotNull Map<Object, Object> computeMap(@NotNull Map<Object, Object> newMap,
                                                   @NotNull Map<?, ?> map,
                                                   @NotNull Function<Object, Object> keyFunction){
        map.forEach((key, value) -> newMap.put(keyFunction.apply(key), value));
        return newMap;
    }

    public <T> @Nullable T deserialize(@NotNull Class<T> clazz, @Nullable YamlElement from){
        Object object = deserializeObject(clazz, from);
        if(object==null) return null;
        if(clazz.isAssignableFrom(object.getClass())) return clazz.cast(object);
        throw new UnsupportedOperationException("Object cannot be cast to " + clazz.getSimpleName() + " (" + object + ")!");
    }

    public @NotNull Object deserializeObject(@NotNull Object o){
        if(o instanceof Collection<?> l) return deserializeCollection(l);
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
