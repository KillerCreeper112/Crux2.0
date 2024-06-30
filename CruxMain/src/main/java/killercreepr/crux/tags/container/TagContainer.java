package killercreepr.crux.tags.container;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.tags.TagsPrefixBuilder;
import killercreepr.crux.tags.context.FormatPrefix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public interface TagContainer<T> extends Iterable<T> {
    default TagContainer<T> hookAll(@NotNull DataExchange info){
        info.forEach(this::hook);
        return this;
    }
    TagContainer<T> hook(@Nullable Object info);
    TagContainer<T> hook(@Nullable Object info, @Nullable TagsPrefixBuilder prefix);
    TagContainer<T> add(@NotNull T resolver);
    TagContainer<T> add(@NotNull T resolver, @Nullable FormatPrefix prefix);
    TagContainer<T> addAll(@Nullable TagContainer<T> tags);
    TagContainer<T> addAll(@Nullable Collection<T> tags);
    TagContainer<T> addAll(@Nullable Collection<T> tags, @Nullable FormatPrefix prefix);
    TagContainer<T> addExact(@NotNull T resolver, @NotNull String id);

    TagContainer<T> addAll(@Nullable TagContainer<T> tags, @Nullable FormatPrefix prefix);
    @Nullable T get(@NotNull String id);
    @NotNull
    Map<String, T> asMap();
}
