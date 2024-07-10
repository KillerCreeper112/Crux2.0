package killercreepr.crux.registry.index;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public interface Index<K, V> {
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

