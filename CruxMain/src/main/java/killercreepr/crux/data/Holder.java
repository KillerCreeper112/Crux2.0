package killercreepr.crux.data;

import killercreepr.crux.registry.MappedRegistry;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

public interface Holder <T>{
    static @NotNull Direct<Object> directObject(@Nullable Object o){
        return new Direct<>(o);
    }
    static <E> @NotNull Direct<E> direct(@Nullable E object){
        return new Direct<>(object);
    }
    static <E> @NotNull Holder<E> weakReference(@Nullable E object){
        return new Holder<>() {
            private final WeakReference<E> reference = new WeakReference<>(object);

            @Override
            public @Nullable E value() {
                return reference.get();
            }
        };
    }
    static @NotNull Holder<Object> emptyObject(){
        return direct(null);
    }

    static <E> @NotNull Holder<E> empty(){
        return direct(null);
    }

    static <K, E> Holder<E> registry(@NotNull K key, @NotNull MappedRegistry<K, E> registry){
        return new Registry<>(key, registry);
    }

    T value();

    default T valueOr(T defaultValue){
        T t = value();
        if(t==null) return defaultValue;
        return t;
    }

    record Direct<T>(T value) implements Holder<T> {
        @Override
        public T value() {
            return this.value;
        }
    }

    record Registry<K, V>(K key, MappedRegistry<K, V> registry) implements Holder<V>{
        @Override
        public V value() {
            return registry.get(key);
        }
    }
}
