package killercreepr.crux.tags.provider;

import killercreepr.crux.Crux;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.StringListTagContainer;
import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.resolver.StringListResolver;
import killercreepr.crux.tags.resolver.StringResolver;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface StringListTagProvider {
    @Contract("null -> null")
    static @Nullable StringListTagProvider build(@Nullable StringListTagContainer tags){
        if(tags==null) return null;
        return () -> tags;
    }
    static @Nullable StringListTagProvider merge(@Nullable TagContainer<StringListResolver> first, @Nullable TagContainer<StringListResolver> second){
        if(first == null){
            if(second==null) return null;
            return () -> second;
        }
        return () -> first.addAll(second);
    }

    static @Nullable StringListTagProvider merge(@Nullable StringListTagProvider first, @Nullable StringListTagProvider second){
        if(first == null && second == null) return null;
        if(first == null) return second;
        if(second == null) return first;

        TagParser tagParser = first.getStringListTags().getTagParser();

        TagContainer<StringListResolver> container = TagContainer.stringList(tagParser);
        container.addAll(first.getStringListTags()).addAll(second.getStringListTags());;
        return () -> container;
    }

    static @Nullable StringListTagProvider merge(@Nullable StringListTagProvider first, @Nullable TagContainer<StringListResolver> second){
        if(first == null && second == null) return null;
        TagContainer<StringListResolver> container = TagContainer.stringList(second==null? Crux.TAGS:second.getTagParser());
        container.addAll(first==null?null:first.getStringListTags());
        return () -> container;
    }

    static @Nullable StringListTagProvider mergeHook(@Nullable StringListTagProvider first, @Nullable Object... hookObjects){
        if(first == null && hookObjects == null) return null;
        TagContainer<StringListResolver> container = TagContainer.stringList(first==null?Crux.TAGS:first.getStringListTags().getTagParser());
        container.addAll(first==null?null:first.getStringListTags());
        for(Object o : hookObjects){
            if(o==null) continue;
            container.hook(o);
        }
        return () -> container;
    }
    @NotNull
    TagContainer<StringListResolver> getStringListTags();
}
