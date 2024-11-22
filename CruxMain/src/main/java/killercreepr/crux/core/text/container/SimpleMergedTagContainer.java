package killercreepr.crux.core.text.container;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.resolver.StringListResolver;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.resolver.TagResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.TagsPrefixBuilder;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.api.text.tags.container.TagContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleMergedTagContainer implements MergedTagContainer {
    protected final @NotNull TagParser tags;
    protected final @NotNull TagContainer<StringResolver> strings;
    protected final @NotNull TagContainer<StringListResolver> stringLists;
    public SimpleMergedTagContainer(@NotNull TagParser tags){
        this(tags, new StringTagContainer(tags), new StringListTagContainer(tags));
    }
    public SimpleMergedTagContainer(@NotNull SimpleMergedTagContainer tags){
        this(tags.getTagParser());
        addAll(tags);
    }
    public SimpleMergedTagContainer(@NotNull TagParser tags, @NotNull TagContainer<StringResolver> strings, @NotNull TagContainer<StringListResolver> stringLists) {
        this.tags = tags;
        this.strings = strings;
        this.stringLists = stringLists;
    }

    @Override
    public String toString() {
        return "SimpleMergedTagContainer{tags=" + tags + ", strings=" + strings + ", stringLists=" + stringLists + "}";
    }

    @Override
    public @NotNull TagContainer<StringListResolver> getStringListTags() {
        return stringLists;
    }

    @Override
    public @NotNull TagContainer<StringResolver> getStringTags() {
        return strings;
    }

    @Override
    public SimpleMergedTagContainer hook(@Nullable Object info) {
        strings.hook(info);
        stringLists.hook(info);
        return this;
    }

    @Override
    public MergedTagContainer hook(@Nullable Object info, @Nullable TagsPrefixBuilder prefix) {
        strings.hook(info, prefix);
        stringLists.hook(info, prefix);
        return this;
    }

    @Override
    public MergedTagContainer hookAll(@Nullable DataExchange info) {
        if(info==null) return this;
        for(Holder<?> o : info){
            hook(o.value());
        }
        return this;
    }

    @Override
    public SimpleMergedTagContainer add(@NotNull TagResolver<?> resolver){
        return add(resolver, null);
    }
    @Override
    public SimpleMergedTagContainer add(@NotNull TagResolver<?> resolver, @Nullable FormatPrefix prefix) {
        if(resolver instanceof StringResolver r){
            strings.add(r, prefix);
        }else if(resolver instanceof StringListResolver r){
            stringLists.add(r, prefix);
        }
        return this;
    }
    @Override
    public SimpleMergedTagContainer addAll(@Nullable TagResolver<?>... resolvers){
        if(resolvers==null) return this;
        for(TagResolver<?> t : resolvers){
            if(t==null) continue;
            add(t);
        }
        return this;
    }
    @Override
    public SimpleMergedTagContainer addAll(@Nullable TagContainer<?> tags) {
        return addAll(tags, null);
    }
    @Override
    public SimpleMergedTagContainer addAll(@Nullable TagContainer<?> tags, @Nullable FormatPrefix prefix) {
        try{
            strings.addAll((TagContainer<StringResolver>) tags, prefix);
        }catch (ClassCastException ignored){
            try{
                stringLists.addAll((TagContainer<StringListResolver>) tags, prefix);
            }catch (ClassCastException ignored1){
            }
        }
        return this;
    }

    @Override
    public SimpleMergedTagContainer addAll(@Nullable MergedTagContainer container){
        if(container==null) return this;
        strings.addAll(container.getStringTags());
        stringLists.addAll(container.getStringListTags());
        return this;
    }

    @Override
    public @NotNull TagParser getTagParser() {
        return tags;
    }
}
