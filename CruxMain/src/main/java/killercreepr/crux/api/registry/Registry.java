package killercreepr.crux.api.registry;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Represents a registered collection of objects.
 */
public interface Registry<T> extends Iterable<T> {
    @NotNull Stream<T> stream();
    <E extends T> E register(@NotNull E object);
    boolean unregister(@NotNull T object);
    void clear();
    @NotNull
    Collection<T> values();
    default boolean isEmpty(){
        return values().isEmpty();
    }
}

