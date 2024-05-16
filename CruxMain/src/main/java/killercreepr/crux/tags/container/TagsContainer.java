package killercreepr.crux.tags.container;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface TagsContainer<T> {
    TagsContainer<T> put(@NotNull String id, @Nullable T tag);
    TagsContainer<T> putIfAbsent(@NotNull String id, @Nullable T tag);
    TagsContainer<T> remove(@NotNull String id);
    boolean has(@NotNull String id);
    @Nullable T get(@NotNull String id);
    @NotNull Map<String, T> get();

    /**
     * @return new TagsContainer(this); Does not clone the data objects themselves.
     */
    @NotNull TagsContainer<T> clone();
}
