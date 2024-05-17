package killercreepr.cruxconfig.config.common.yaml;

import killercreepr.cruxconfig.config.common.yaml.annotation.YamlSerializer;
import killercreepr.cruxconfig.config.common.yaml.annotation.YamlSerializerID;
import killercreepr.cruxconfig.config.common.yaml.container.YamlContainerHandler;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlContainerHandlerRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class YamlRegistry {
    public final String DESERIALIZE_METHOD_NAME = "deserializeFromYaml";
    public final Map<String, Class<? extends YamlSerializable>> REGISTRY = new HashMap<>();
    public final YamlContainerHandlerRegistry CONTAINER_REGISTRY = new YamlContainerHandlerRegistry(this);

    public YamlRegistry() {
    }

    public <T extends YamlSerializable> void register(@NotNull String name, @NotNull Class<T> clazz){
        REGISTRY.put(name, clazz);
    }

    public <T extends YamlContainerHandler<?>> void registerContainerHandler(@NotNull Class<?> clazz, @NotNull T object){
        CONTAINER_REGISTRY.register(clazz, object);
    }

    @SafeVarargs
    public final void register(@NotNull Class<? extends YamlSerializable>... clazzes){
        for(Class<? extends YamlSerializable> clazz : clazzes){
            register(clazz);
        }
    }

    public @NotNull String getSerializerID(@NotNull Object object){
        if(object instanceof YamlSerializerID i) return i.yamlSerializerID();
        return getSerializeID(object.getClass());
    }

    public <T> @NotNull String getSerializeID(@NotNull Class<T> clazz){
        YamlSerializer serializable = clazz.getAnnotation(YamlSerializer.class);
        if (serializable == null){
            Class<?> superclass = clazz.getSuperclass();
            while(superclass != null){
                serializable = superclass.getAnnotation(YamlSerializer.class);
                if(serializable != null) return serializable.id();
                superclass = superclass.getSuperclass();
            }
            throw new RuntimeException(clazz + " is not annotated with JsonSerializer!");
        }
        return serializable.id();
    }

    public <T extends YamlSerializable> void register(@NotNull Class<T> clazz){
        register(getSerializeID(clazz), clazz);
    }
    public @Nullable Class<? extends YamlSerializable> unregister(@NotNull String name){
        return REGISTRY.remove(name);
    }

    public @Nullable YamlContainerHandler<?> getContainerHandler(@NotNull Class<?> clazz){
        return CONTAINER_REGISTRY.get(clazz);
    }

    public @Nullable Class<? extends YamlSerializable> get(@NotNull String name){
        return REGISTRY.get(name);
    }

    public <T extends YamlSerializable> @Nullable Class<? extends YamlSerializable> get(@NotNull Class<T> from){
        return get(getSerializeID(from));
    }

    public @Nullable YamlContainerHandler<?> findContainerHandler(@NotNull Class<?> from){
        for(Map.Entry<Class<?>, YamlContainerHandler<?>> entry : CONTAINER_REGISTRY.entrySet()){
            Class<?> clazz = entry.getKey();
            if(clazz.isAssignableFrom(from)){
                return entry.getValue();
            }
        }
        return null;
    }

    public <T extends YamlSerializable> @NotNull YamlElement serialize(@NotNull T object){
        YamlContainerHandler<?> handler = findContainerHandler(object.getClass());
        return handler.attemptSerializeToYaml(new YamlContext(this), object);
    }

    public @Nullable Object deserialize(@NotNull Class<?> clazz, @Nullable YamlElement from){
        YamlContainerHandler<?> handler = findContainerHandler(clazz);
        return handler == null ? null : handler.deserializeFromYaml(new YamlContext(this), from);
    }

    public <T> @Nullable T deserializeExact(@NotNull Class<T> clazz, @Nullable YamlElement from){
        YamlContainerHandler<?> handler = findContainerHandler(clazz);
        return handler == null ? null : (T) handler.deserializeFromYaml(new YamlContext(this), from);
    }

}
