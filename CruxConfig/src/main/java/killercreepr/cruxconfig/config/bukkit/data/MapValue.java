package killercreepr.cruxconfig.config.bukkit.data;

import killercreepr.crux.util.CruxReflect;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

public class MapValue extends CommonValue<Map<?, ?>> {
    protected final Class<?> firstType;
    public MapValue(@Nullable Map<?, ?> defaultValue, Class<?> firstType) {
        super((Class<Map<?,?>>) (Class<?>) Map.class, defaultValue);
        this.firstType = firstType;
    }

    public MapValue(@NotNull Class<?> firstType) {
        this(null, firstType);
    }

    @Override
    public @Nullable Map<?, ?> get(@NotNull CruxConfig cfg, @NotNull String path) {
        Object object = cfg.deserialize(type, path);
        if(object==null) return null;
        return (Map<?, ?>) convertObject(firstType, object);
    }

    public @NotNull Object convertObject(@NotNull Class<?> clazz, @NotNull Object object){
        if(object instanceof Map<?,?> map){
            return convertMap(clazz, map);
        }
        return object;
    }

    public @NotNull Map<?, ?> convertMap(@NotNull Class<?> first, @NotNull Map<?, ?> map){
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
}
