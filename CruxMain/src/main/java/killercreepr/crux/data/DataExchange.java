package killercreepr.crux.data;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public interface DataExchange extends Iterable<Holder<?>> {
    DataExchange EMPTY = new SimpleDataExchange(Map.of());

    static @NotNull Builder builder() {
        return new SimpleDataExchange.Builder();
    }

    static @NotNull DataExchange empty() {
        return EMPTY;
    }

    static @NotNull DataExchange single(@NotNull String id, @NotNull Holder<?> holder) {
        return new SimpleDataExchange(id, holder);
    }

    boolean isEmpty();

    /**
     * @return A new DataExchange with the appended info.
     */
    @Contract(pure = true)
    @NotNull
    DataExchange append(@NotNull DataExchange info);

    /**
     * @return A new DataExchange with the appended info.
     */
    @Contract(pure = true)
    @NotNull
    DataExchange append(@NotNull Map<String, Holder<?>> info);

    /**
     * @return A new DataExchange with the appended object.
     */
    @Contract(pure = true)
    @NotNull
    DataExchange append(@NotNull String id, @NotNull Holder<?> object);

    /**
     * @return A new DataExchange with the removed values.
     */
    @Contract(pure = true)
    @NotNull
    DataExchange removeIf(@NotNull Predicate predicate);

    boolean has(@NotNull String id);

    <T> @NotNull Optional<T> getObject(@NotNull Class<T> findFirst);

    <T> @Nullable T get(@NotNull Class<T> findFirst);

    <T> @NotNull Map<String, T> getObjects(@NotNull Class<T> findAll);

    @NotNull
    Optional<Object> getObject(@NotNull String id);

    <T> @NotNull Optional<T> getObject(@NotNull String id, @NotNull Class<T> find);

    @Nullable
    Object get(@NotNull String id);

    <T> @Nullable T get(@NotNull String id, @NotNull Class<T> find);

    //Convenience methods.
    <T> @NotNull T getOrThrow(@NotNull Class<T> find);

    @NotNull
    Object getOrThrow(@NotNull String id);

    <T> @NotNull T getOrThrow(@NotNull String id, @NotNull Class<T> find);

    <T> T getOrDefault(@NotNull Class<T> find, @Nullable T defaultValue);

    Object getOrDefault(@NotNull String id, @Nullable Object defaultValue);

    <T> T getOrDefault(@NotNull String id, @NotNull Class<T> type, @Nullable T defaultValue);

    /**
     * @return An immutable map containing this DataExchange's data.
     */
    @NotNull
    Map<String, Holder<?>> asMap();

    @NotNull
    @Override
    Iterator<Holder<?>> iterator();

    interface Predicate {
        boolean test(@NotNull String id, @NotNull Holder<?> holder);
    }

    interface Builder {
        Builder putAll(@Nullable Object direct, @NotNull String... ids);

        Builder put(@NotNull String id, @Nullable Object direct);

        Builder put(@NotNull Object direct);

        Builder putAll(@NotNull Holder<?> holder, @NotNull String... ids);

        Builder put(@NotNull String id, @NotNull Holder<?> holder);

        Builder putAll(@Nullable DataExchange info);

        Builder putAll(@NotNull Map<String, Holder<?>> map);

        Builder remove(@NotNull String id);

        @NotNull DataExchange build();
    }
}
