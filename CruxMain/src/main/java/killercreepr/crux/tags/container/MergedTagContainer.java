package killercreepr.crux.tags.container;

import killercreepr.crux.Crux;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.provider.StringListTagProvider;
import killercreepr.crux.tags.provider.StringTagProvider;
import killercreepr.crux.tags.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MergedTagContainer extends StringTagProvider, StringListTagProvider {
    static @Nullable MergedTagContainer merge(@Nullable MergedTagContainer container, @Nullable TagContainer<?> tags){
        if(container == null && tags == null) return null;
        return new MultiTagContainer(container==null? Crux.TAGS:container.getStringTags().getTagParser())
            .addAll(tags);
    }

    static @Nullable MergedTagContainer mergeHook(@Nullable MergedTagContainer container, @Nullable Object... hookObjects){
        if(container == null && hookObjects == null) return null;
        MultiTagContainer merged = new MultiTagContainer(container==null?Crux.TAGS:container.getStringTags().getTagParser());
        for(Object o : hookObjects){
            if(o==null) continue;
            merged.hook(o);
        }
        return merged;
    }
    MergedTagContainer hook(@Nullable Object info);

    MergedTagContainer add(@NotNull TagResolver<?> resolver);
    MergedTagContainer add(@NotNull TagResolver<?> resolver, @Nullable FormatPrefix prefix);

    MergedTagContainer addAll(@Nullable TagResolver<?>... resolvers);
    MergedTagContainer addAll(@Nullable TagContainer<?> tags);
    MergedTagContainer addAll(@Nullable TagContainer<?> tags, @Nullable FormatPrefix prefix);

    MergedTagContainer add(@NotNull MergedTagContainer container);
}
