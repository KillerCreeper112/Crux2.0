package killercreepr.crux.api.registry.index;

import killercreepr.crux.core.registry.index.SimpleIndex;
import killercreepr.crux.core.util.CruxMap;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;

public interface Index<K, V> {
    static <K, V> @NotNull Index<K, V> index(@NotNull Map<K, V> constant){
        return new SimpleIndex<>(
            Collections.unmodifiableMap(constant), Collections.unmodifiableMap(CruxMap.reverse(constant))
        );
    }

    static <K, V extends Enum<V>> @NotNull Index<K, V> index(final Class<V> type, final @NotNull Function<? super V, ? extends K> keyFunction) {
        return index(type, keyFunction, type.getEnumConstants());
    }

    @SafeVarargs
    static <K, V extends Enum<V>> @NotNull Index<K, V> index(final Class<V> type, final @NotNull Function<? super V, ? extends K> keyFunction, final @NotNull V... values) {
        return index(values, ((length) -> new EnumMap<>(type)), keyFunction);
    }

    @SafeVarargs
    static <K, V> @NotNull Index<K, V> index(final @NotNull Function<? super V, ? extends K> keyFunction, final @NotNull V... values) {
        return index(values, HashMap::new, keyFunction);
    }

    static <K, V> @NotNull Index<K, V> index(final @NotNull Function<? super V, ? extends K> keyFunction, final @NotNull List<V> constants) {
        return index(constants, HashMap::new, keyFunction);
    }

    private static <K, V> @NotNull Index<K, V> index(final V[] values, final IntFunction<Map<V, K>> valueToKeyFactory, final @NotNull Function<? super V, ? extends K> keyFunction) {
        return index(Arrays.asList(values), valueToKeyFactory, keyFunction);
    }

    private static <K, V> @NotNull Index<K, V> index(final List<V> values, final IntFunction<Map<V, K>> valueToKeyFactory, final @NotNull Function<? super V, ? extends K> keyFunction) {
        int length = values.size();
        Map<K, V> keyToValue = new HashMap<>(length);
        Map<V, K> valueToKey = valueToKeyFactory.apply(length);

        for(int i = 0; i < length; ++i) {
            V value = values.get(i);
            K key = keyFunction.apply(value);
            if (keyToValue.putIfAbsent(key, value) != null) {
                throw new IllegalStateException(String.format("Key %s already mapped to value %s", key, keyToValue.get(key)));
            }

            if (valueToKey.putIfAbsent(value, key) != null) {
                throw new IllegalStateException(String.format("Value %s already mapped to key %s", value, valueToKey.get(value)));
            }
        }

        return new SimpleIndex<>(Collections.unmodifiableMap(keyToValue), Collections.unmodifiableMap(valueToKey));
    }

    @NotNull Set<K> keys();

    @Nullable K key(final @NotNull V value);

    default @NotNull K keyOrThrow(final @NotNull V value) {
        K k = this.key(value);
        if(k==null) throw new RuntimeException("Key is not present!");
        return k;
    }

    @Contract("_, null -> null; _, !null -> !null")
    default K keyOr(final @NotNull V value, final @Nullable K defaultKey) {
        K key = this.key(value);
        return key == null ? defaultKey : key;
    }

    @NotNull Set<V> values();

    @Nullable V value(final @NotNull K key);

    default @NotNull V valueOrThrow(final @NotNull K key){
        V v = this.value(key);
        if(v==null) throw new RuntimeException("Value is not present!");
        return v;
    }

    @Contract("_, null -> null; _, !null -> !null")
    default V valueOr(final @NotNull K key, final @Nullable V defaultValue) {
        V value = this.value(key);
        return value == null ? defaultValue : value;
    }
    @NotNull Map<K, V> keyToValue();
    @NotNull Map<V, K> valueToKey();
}

