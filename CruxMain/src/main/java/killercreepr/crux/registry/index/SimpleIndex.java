package killercreepr.crux.registry.index;

import killercreepr.crux.util.CruxMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;

public class SimpleIndex<K, V> implements Index<K, V> {
    public static <K, V> @NotNull Index<K, V> create(@NotNull Map<K, V> constant){
        return new SimpleIndex<>(
            Collections.unmodifiableMap(constant), Collections.unmodifiableMap(CruxMap.reverse(constant))
        );
    }

    public static <K, V extends Enum<V>> @NotNull Index<K, V> create(final Class<V> type, final @NotNull Function<? super V, ? extends K> keyFunction) {
        return create(type, keyFunction, type.getEnumConstants());
    }

    @SafeVarargs
    public static <K, V extends Enum<V>> @NotNull Index<K, V> create(final Class<V> type, final @NotNull Function<? super V, ? extends K> keyFunction, final @NotNull V... values) {
        return create(values, ((length) -> new EnumMap<>(type)), keyFunction);
    }

    @SafeVarargs
    public static <K, V> @NotNull Index<K, V> create(final @NotNull Function<? super V, ? extends K> keyFunction, final @NotNull V... values) {
        return create(values, HashMap::new, keyFunction);
    }

    public static <K, V> @NotNull Index<K, V> create(final @NotNull Function<? super V, ? extends K> keyFunction, final @NotNull List<V> constants) {
        return create(constants, HashMap::new, keyFunction);
    }

    private static <K, V> @NotNull Index<K, V> create(final V[] values, final IntFunction<Map<V, K>> valueToKeyFactory, final @NotNull Function<? super V, ? extends K> keyFunction) {
        return create(Arrays.asList(values), valueToKeyFactory, keyFunction);
    }

    private static <K, V> @NotNull Index<K, V> create(final List<V> values, final IntFunction<Map<V, K>> valueToKeyFactory, final @NotNull Function<? super V, ? extends K> keyFunction) {
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

    private final Map<K, V> keyToValue;
    private final Map<V, K> valueToKey;

    private SimpleIndex(final Map<K, V> keyToValue, final Map<V, K> valueToKey) {
        this.keyToValue = keyToValue;
        this.valueToKey = valueToKey;
    }
    @Override
    public @NotNull Set<K> keys() {
        return Collections.unmodifiableSet(this.keyToValue.keySet());
    }
    @Override
    public @Nullable K key(final @NotNull V value) {
        return this.valueToKey.get(value);
    }
    @Override
    public @NotNull Set<V> values() {
        return Collections.unmodifiableSet(this.valueToKey.keySet());
    }
    @Override
    public @Nullable V value(final @NotNull K key) {
        return this.keyToValue.get(key);
    }
    @Override
    public @NotNull Map<K, V> keyToValue() {
        return Collections.unmodifiableMap(this.keyToValue);
    }

    @Override
    public @NotNull Map<V, K> valueToKey() {
        return Collections.unmodifiableMap(this.valueToKey);
    }
}
