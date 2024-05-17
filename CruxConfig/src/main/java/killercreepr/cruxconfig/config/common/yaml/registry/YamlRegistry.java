package killercreepr.cruxconfig.config.common.yaml.registry;

import killercreepr.crux.valueproviders.number.ConstantNumber;
import killercreepr.crux.valueproviders.number.EquationNumber;
import killercreepr.crux.valueproviders.number.UniformNumber;
import killercreepr.cruxconfig.config.common.yaml.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.automatic.AutoYamlSerializer;
import killercreepr.cruxconfig.config.common.yaml.container.YamlObjectHandler;
import killercreepr.cruxconfig.config.common.yaml.element.*;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

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
        Bukkit.getLogger().warning("FINDING POTENTIAL handlers: " + from.getSimpleName());
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
            Bukkit.getLogger().warning("AYO CHECKING THIS RIGHT NOW: " + clazz.getSimpleName() + " POTENTIAL: " + ((AutoYamlSerializer)handler).getType().getSimpleName());
            Object o = handler.deserializeFromYaml(new YamlContext(this), from);
            if(o!=null) return o;
        }
        return from==null?null: from.getAsObject();
    }
    //YamlObjectHandler<?> handler = findContainerHandler(clazz);
    //        /*for(YamlObjectHandler<?> handler : findPotentialHandlers(clazz)){
    //            Object o = handler.deserializeFromYaml(new YamlContext(this), from);
    //            if(o==null) continue;
    //            return o;
    //        }*/
    //        if(handler==null) return null;
    //        return handler.deserializeFromYaml(new YamlContext(this), from);

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
