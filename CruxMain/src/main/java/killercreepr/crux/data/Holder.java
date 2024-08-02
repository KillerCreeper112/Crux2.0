package killercreepr.crux.data;

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
}
