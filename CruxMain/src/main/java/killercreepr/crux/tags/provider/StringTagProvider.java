package killercreepr.crux.tags.provider;

import killercreepr.crux.Crux;
import killercreepr.crux.tags.container.StringTagContainer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StringTagProvider {
    @Contract("null -> null")
    static @Nullable StringTagProvider build(@Nullable StringTagContainer tags){
        if(tags==null) return null;
        return () -> tags;
    }
    static @Nullable StringTagProvider merge(@Nullable StringTagContainer first, @Nullable StringTagContainer second){
        if(first == null){
            if(second==null) return null;
            return () -> second;
        }
        return () -> first.addAll(second);
    }

    static @Nullable StringTagProvider merge(@Nullable StringTagProvider first, @Nullable StringTagContainer second){
        if(first == null && second == null) return null;
        StringTagContainer container = new StringTagContainer(second==null?Crux.TAGS:second.getTagParser());
        container.addAll(first==null?null:first.getStringTags());
        return () -> container;
    }

    static @Nullable StringTagProvider mergeHook(@Nullable StringTagProvider first, @Nullable Object... hookObjects){
        if(first == null && hookObjects == null) return null;
        StringTagContainer container = new StringTagContainer(first==null?Crux.TAGS:first.getStringTags().getTagParser());
        container.addAll(first==null?null:first.getStringTags());
        for(Object o : hookObjects){
            if(o==null) continue;
            container.hook(o);
        }
        return () -> container;
    }
    @NotNull StringTagContainer getStringTags();
}
