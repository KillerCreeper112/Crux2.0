package killercreepr.crux.api.data;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import killercreepr.crux.api.registry.MappedRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Holder <T>{
    Holder<Object> EMPTY = direct(null);
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
        return EMPTY;
    }

    static <E> @NotNull Holder<E> empty(){
        return (Holder<E>) EMPTY;
    }

    static <K, E> Holder<E> registry(@NotNull K key, @NotNull MappedRegistry<K, E> registry){
        return new Registry<>(key, registry);
    }

    T value() throws CommandSyntaxException;

    default Holder<T> ifPresent(@NotNull Consumer<T> consumer){
        T value = value();
        if(value == null) return this;
        consumer.accept(value);
        return this;
    }

    default T valueOrThrow() {
        T value = value();
        if (value == null) throw new NoSuchElementException("No value present");
        return value;
    }

    default  <X extends Throwable> T valueOrThrow(Supplier<? extends X> exceptionSupplier) throws X {
        T value = value();
        if(value != null) return value;
        throw exceptionSupplier.get();
    }

    default T valueOr(T defaultValue){
        T t = value();
        if(t==null) return defaultValue;
        return t;
    }

    default T valueOrSupply(Supplier<T> supplier){
        T t = value();
        if(t==null) return supplier.get();
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
