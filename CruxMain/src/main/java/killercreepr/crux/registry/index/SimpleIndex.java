package killercreepr.crux.registry.index;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class SimpleIndex<K, V> implements Index<K, V> {
    private final Map<K, V> keyToValue;
    private final Map<V, K> valueToKey;

    protected SimpleIndex(final Map<K, V> keyToValue, final Map<V, K> valueToKey) {
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
