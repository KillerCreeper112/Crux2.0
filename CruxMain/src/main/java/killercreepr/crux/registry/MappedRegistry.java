package killercreepr.crux.registry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * Represents a registered collection of mapped objects. KEY -> OBJECT
 */
public interface MappedRegistry<K, V> extends Registry<V>{
    V getOrDefault(@NotNull K key, @Nullable V defaultValue);
    @Nullable V get(@NotNull K key);
    <E extends V> @NotNull E register(@NotNull K key, @NotNull E value);
    @Nullable V remove(@NotNull K key);
    boolean remove(@NotNull K key, @NotNull V value);
    boolean containsKey(@NotNull K key);
    boolean containsValue(@NotNull V value);
    @NotNull Set<Map.Entry<K, V>> entrySet();

    default <T extends V> @Nullable T get(@NotNull Class<T> type, @NotNull K key){
        V object = get(key);
        if(object == null) return null;
        if(type.isAssignableFrom(object.getClass())) return type.cast(object);
        return null;
    }
}

