package killercreepr.crux.api.registry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents a registered collection of mapped objects. KEY -> OBJECT
 */
public interface MappedRegistry<K, V> extends Registry<V> {
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

    default V compute(K key,
                      BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        V oldValue = get(key);

        V newValue = remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            // delete mapping
            if (oldValue != null || containsKey(key)) {
                // something to remove
                remove(key);
            }
            return null;
        } else {
            // add or replace old mapping
            register(key, newValue);
            return newValue;
        }
    }

    default V computeIfAbsent(K key,
                              Function<? super K, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        V v;
        if ((v = get(key)) == null) {
            V newValue;
            if ((newValue = mappingFunction.apply(key)) != null) {
                register(key, newValue);
                return newValue;
            }
        }

        return v;
    }

    default V computeIfPresent(K key,
                               BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        V oldValue;
        if ((oldValue = get(key)) != null) {
            V newValue = remappingFunction.apply(key, oldValue);
            if (newValue != null) {
                register(key, newValue);
                return newValue;
            } else {
                remove(key);
                return null;
            }
        } else {
            return null;
        }
    }
}

