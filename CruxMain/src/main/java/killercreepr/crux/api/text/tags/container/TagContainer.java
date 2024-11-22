package killercreepr.crux.api.text.tags.container;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.provider.StringListTagProvider;
import killercreepr.crux.api.text.provider.StringTagProvider;
import killercreepr.crux.api.text.resolver.StringListResolver;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.resolver.TagResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.TagsPrefixBuilder;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.text.container.SimpleMergedTagContainer;
import killercreepr.crux.core.text.container.SimpleStringListTagProvider;
import killercreepr.crux.core.text.container.SimpleStringTagProvider;
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

    static @Nullable MergedTagContainer merged(@Nullable StringTagProvider strings){
        return strings == null ? null : merged().addAll(strings.getStringTags());
    }

    static @Nullable MergedTagContainer merged(@Nullable StringListTagProvider strings){
        return strings == null ? null : merged().addAll(strings.getStringListTags());
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
