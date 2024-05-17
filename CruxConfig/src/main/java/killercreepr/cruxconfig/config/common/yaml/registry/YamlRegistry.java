package killercreepr.cruxconfig.config.common.yaml.registry;

import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.yaml.NumTest;
import killercreepr.cruxconfig.config.common.yaml.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.container.YamlObjectHandler;
import killercreepr.cruxconfig.config.common.yaml.element.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class YamlRegistry {
    public final YamlObjectHandlerRegistry HANDLER_REGISTRY = new YamlObjectHandlerRegistry(this);

    public YamlRegistry() {
        registerHandler(NumberProvider.class, new NumTest());
    }

    public <T extends YamlObjectHandler<?>> void registerHandler(@NotNull Class<?> clazz, @NotNull T object){
        HANDLER_REGISTRY.register(clazz, object);
    }

    public @Nullable YamlObjectHandler<?> getContainerHandler(@NotNull Class<?> clazz){
        return HANDLER_REGISTRY.get(clazz);
    }

    public @Nullable YamlObjectHandler<?> findContainerHandler(@NotNull Class<?> from){
        for(Map.Entry<Class<?>, YamlObjectHandler<?>> entry : HANDLER_REGISTRY.entrySet()){
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
        YamlObjectHandler<?> handler = findContainerHandler(clazz);
        if(handler==null) return null;
        return handler.deserializeFromYaml(new YamlContext(this), from);
    }

    public <T> @Nullable T deserialize(@NotNull Class<T> clazz, @Nullable YamlElement from){
        Object object = deserializeObject(clazz, from);
        if(object==null) return null;
        if(clazz.isAssignableFrom(object.getClass())) return clazz.cast(object);
        throw new UnsupportedOperationException("Object cannot be cast to " + clazz.getSimpleName() + " (" + object + ")!");
    }

    public @NotNull YamlElement serializeObject(@NotNull Object o){
        if(o instanceof YamlElement d) return d;
        if(o instanceof String s) return new YamlPrimitive(s);
        if(o instanceof Number s) return new YamlPrimitive(s);
        if(o instanceof Boolean s) return new YamlPrimitive(s);
        if(o instanceof Collection<?> l) return convertCollection(l);
        if(o.getClass().isArray()){
            return convertCollection(Arrays.stream(((Object[]) o)).toList());
        }
        if(o instanceof Map<?,?> l) return convertMap(l);
        YamlElement serialized = serialize(o);
        if(serialized != null) return serialized;
        return new YamlGeneric(o);
    }

    public @NotNull YamlArray convertCollection(@NotNull Collection<?> list){
        YamlArray array = new YamlArray(list.size());
        for(Object o : list){
            array.add(serializeObject(o));
        }
        return array;
    }

    public @NotNull YamlObject convertMap(@NotNull Map<?, ?> list){
        YamlObject array = new YamlObject();
        for(Map.Entry<?, ?> entry : list.entrySet()){
            if(!(entry.getKey() instanceof String key)) return array;
            array.add(key, serializeObject(entry.getValue()));
        }
        return array;
    }
}
