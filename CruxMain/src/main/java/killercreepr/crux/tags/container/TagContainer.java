package killercreepr.crux.tags.container;

import killercreepr.crux.Crux;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.TagsPrefixBuilder;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.resolver.StringListResolver;
import killercreepr.crux.tags.resolver.StringResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public interface TagContainer<T> extends Iterable<T> {
    static TagContainer<StringResolver> string(){
        return string(Crux.TAGS);
    }

    static TagContainer<StringListResolver> stringList(){
        return stringList(Crux.TAGS);
    }

    static TagContainer<StringResolver> string(@NotNull TagParser tagParser){
        return new StringTagContainer(tagParser);
    }

    static TagContainer<StringListResolver> stringList(@NotNull TagParser tagParser){
        return new StringListTagContainer(tagParser);
    }

    default TagContainer<T> hookAll(@NotNull DataExchange info){
        info.forEach(e -> hook(e.value()));
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
