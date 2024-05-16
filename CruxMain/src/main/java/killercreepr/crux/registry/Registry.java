package killercreepr.crux.registry;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * Represents a registered collection of objects.
 */
public interface Registry<T> extends Iterable<T>{
    @NotNull Stream<T> stream();
    @NotNull T register(@NotNull T object);
    boolean unregister(@NotNull T object);
    void clear();
}

