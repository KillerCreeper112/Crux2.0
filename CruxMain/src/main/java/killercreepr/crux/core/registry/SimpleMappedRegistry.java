package killercreepr.crux.core.registry;

import killercreepr.crux.api.registry.MappedRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class SimpleMappedRegistry<K, V> implements MappedRegistry<K, V> {
    public static <K, V> @NotNull SimpleMappedRegistry<K, V> fromHashMap(){
        return new SimpleMappedRegistry<>(new HashMap<>());
    }
    public static <K, V> @NotNull SimpleMappedRegistry<K, V> fromConcurrentHashMap(){
        return new SimpleMappedRegistry<>(new ConcurrentHashMap<>());
    }

    protected final Map<K, V> map;
    public SimpleMappedRegistry(@NotNull Map<K, V> map) {
        this.map = map;
    }

    public Map<K, V> getMap() {
        return map;
    }

    /**
     * Creates a registry using a HashMap.
     */
    public SimpleMappedRegistry() {
        this(new HashMap<>());
    }

    @Override
    public @Nullable V getOrDefault(@NotNull K key, @Nullable V defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    @Override
    public @Nullable V get(@NotNull K key) {
        return map.get(key);
    }

    @Override
    public <E extends V> @NotNull E register(@NotNull K key, @NotNull E value) {
        map.put(key, value);
        return value;
    }

    @Override
    public @Nullable V remove(@NotNull K key) {
        return map.remove(key);
    }

    @Override
    public boolean remove(@NotNull K key, @NotNull V value) {
        return map.remove(key, value);
    }

    @Override
    public boolean containsKey(@NotNull K key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(@NotNull V value) {
        return map.containsValue(value);
    }

    @Override
    public @NotNull Set<Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public @NotNull Stream<V> stream() {
        return map.values().stream();
    }

    @Override
    public <E extends V> @NotNull E register(@NotNull E object) {
        throw new UnsupportedOperationException("Mapped registry does not support object register! Please use register(Key, Value)");
    }

    @Override
    public boolean unregister(@NotNull V object) {
        throw new UnsupportedOperationException("Mapped registry does not support object unregister! Please use register(Key, Value)");
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public @NotNull Collection<V> values() {
        return map.values();
    }

    @NotNull
    @Override
    public Iterator<V> iterator() {
        return map.values().iterator();
    }
}
