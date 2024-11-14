package killercreepr.crux.tags.container;

import killercreepr.crux.Crux;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.TagsPrefixBuilder;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.provider.StringListTagProvider;
import killercreepr.crux.tags.provider.StringTagProvider;
import killercreepr.crux.tags.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MergedTagContainer extends StringTagProvider, StringListTagProvider {
    @Deprecated
    static @NotNull MergedTagContainer standard(){
        return createNew(Crux.tags());
    }
    @Deprecated
    static @NotNull MergedTagContainer standard(@Nullable TagResolver<?>... resolvers){
        return standard().addAll(resolvers);
    }
    @Deprecated
    static @NotNull MergedTagContainer createNew(@NotNull TagParser tags){
        return new SimpleMergedTagContainer(tags);
    }
    @Deprecated
    static @NotNull MergedTagContainer createNew(@NotNull TagParser tags, @Nullable TagResolver<?> resolvers){
        return createNew(tags).addAll(resolvers);
    }

    static @Nullable MergedTagContainer merge(@Nullable MergedTagContainer first, @Nullable MergedTagContainer second){
        if(first == null && second == null) return null;
        if(first == null) return second;
        if(second == null) return first;
        TagParser tagParser = first.getStringTags().getTagParser();

        MergedTagContainer container = TagContainer.merged(tagParser);
        container.addAll(first.getStringTags()).addAll(second.getStringTags());
        return container;
    }

    static @Nullable MergedTagContainer merge(@Nullable MergedTagContainer container, @Nullable TagContainer<?> tags){
        if(container == null && tags == null) return null;
        return new SimpleMergedTagContainer(container==null? Crux.tags():container.getStringTags().getTagParser())
            .addAll(tags);
    }

    static @Nullable MergedTagContainer mergeHook(@Nullable MergedTagContainer container, @Nullable Object... hookObjects){
        if(container == null && hookObjects == null) return null;
        if(hookObjects==null) return container;
        SimpleMergedTagContainer merged = new SimpleMergedTagContainer(container==null?Crux.tags():container.getStringTags().getTagParser());
        merged.addAll(container);
        for(Object o : hookObjects){
            if(o==null) continue;
            merged.hook(o);
        }
        return merged;
    }

    static @Nullable MergedTagContainer mergeHook(@Nullable MergedTagContainer container, @Nullable DataExchange data){
        if(container == null && data == null) return null;
        if(data==null) return container;
        SimpleMergedTagContainer merged = new SimpleMergedTagContainer(container==null?Crux.tags():container.getStringTags().getTagParser());
        merged.addAll(container);
        for(Holder<?> o : data){
            if(o==null) continue;
            merged.hook(o.value());
        }
        return merged;
    }
    MergedTagContainer hook(@Nullable Object info);
    MergedTagContainer hook(@Nullable Object info, @Nullable TagsPrefixBuilder prefix);
    MergedTagContainer hookAll(@Nullable DataExchange info);

    MergedTagContainer add(@NotNull TagResolver<?> resolver);
    MergedTagContainer add(@NotNull TagResolver<?> resolver, @Nullable FormatPrefix prefix);

    MergedTagContainer addAll(@Nullable TagResolver<?>... resolvers);
    MergedTagContainer addAll(@Nullable TagContainer<?> tags);
    MergedTagContainer addAll(@Nullable TagContainer<?> tags, @Nullable FormatPrefix prefix);

    MergedTagContainer addAll(@Nullable MergedTagContainer container);

    @NotNull
    TagParser getTagParser();
}
