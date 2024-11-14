package killercreepr.crux.tags.container;

import killercreepr.crux.Crux;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.TagsPrefixBuilder;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.resolver.StringListResolver;
import killercreepr.crux.tags.resolver.StringResolver;
import killercreepr.crux.tags.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public interface TagContainer<T extends TagResolver<?>> extends Iterable<T> {
    static StringTagContainProvider string(){
        return string(Crux.tags());
    }
    static StringTagContainProvider string(@NotNull TagParser tagParser){
        return new SimpleStringTagProvider(tagParser);
    }

    static StringTagContainProvider string(@Nullable StringResolver... resolvers){
        return string(Crux.tags(), resolvers);
    }

    static StringTagContainProvider string(@NotNull TagParser tagParser, @Nullable StringResolver... resolvers){
        return string(tagParser).addAll(resolvers == null ? null : Arrays.asList(resolvers));
    }

    static StringListTagContainProvider stringList(){
        return stringList(Crux.tags());
    }

    static StringListTagContainProvider stringList(@NotNull TagParser tagParser){
        return new SimpleStringListTagProvider(tagParser);
    }

    static StringListTagContainProvider stringList(@Nullable StringListResolver... resolvers){
        return stringList(Crux.tags(), resolvers);
    }

    static StringListTagContainProvider stringList(@NotNull TagParser tagParser, @Nullable StringListResolver... resolvers){
        return new SimpleStringListTagProvider(tagParser).addAll(resolvers == null ? null : Arrays.asList(resolvers));
    }

    static @NotNull MergedTagContainer merged(){
        return merged(Crux.tags());
    }
    static @NotNull MergedTagContainer merged(@Nullable TagResolver<?>... resolvers){
        return merged().addAll(resolvers);
    }

    static @NotNull MergedTagContainer merged(@NotNull TagParser tags){
        return new SimpleMergedTagContainer(tags);
    }

    static @NotNull MergedTagContainer merged(@NotNull TagParser tags, @Nullable TagResolver<?>... resolvers){
        return merged(tags).addAll(resolvers);
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

    @NotNull TagParser getTagParser();
}
