package killercreepr.crux.tags.container;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.TagsPrefixBuilder;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.resolver.StringListResolver;
import killercreepr.crux.tags.resolver.StringResolver;
import killercreepr.crux.tags.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MultiTagContainer implements MergedTagContainer {
    protected final @NotNull TagParser tags;
    protected final @NotNull StringTagContainer strings;
    protected final @NotNull StringListTagContainer stringLists;
    public MultiTagContainer(@NotNull TagParser tags){
        this(tags, new StringTagContainer(tags), new StringListTagContainer(tags));
    }
    public MultiTagContainer(@NotNull MultiTagContainer tags){
        this(tags.getTagParser());
        addAll(tags);
    }
    public MultiTagContainer(@NotNull TagParser tags, @NotNull StringTagContainer strings, @NotNull StringListTagContainer stringLists) {
        this.tags = tags;
        this.strings = strings;
        this.stringLists = stringLists;
    }

    @Override
    public @NotNull StringListTagContainer getStringListTags() {
        return stringLists;
    }

    @Override
    public @NotNull StringTagContainer getStringTags() {
        return strings;
    }

    @Override
    public MultiTagContainer hook(@Nullable Object info) {
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
    public MultiTagContainer add(@NotNull TagResolver<?> resolver){
        return add(resolver, null);
    }
    @Override
    public MultiTagContainer add(@NotNull TagResolver<?> resolver, @Nullable FormatPrefix prefix) {
        if(resolver instanceof StringResolver r){
            strings.add(r, prefix);
        }else if(resolver instanceof StringListResolver r){
            stringLists.add(r, prefix);
        }
        return this;
    }
    @Override
    public MultiTagContainer addAll(@Nullable TagResolver<?>... resolvers){
        if(resolvers==null) return this;
        for(TagResolver<?> t : resolvers){
            if(t==null) continue;
            add(t);
        }
        return this;
    }
    @Override
    public MultiTagContainer addAll(@Nullable TagContainer<?> tags) {
        return addAll(tags, null);
    }
    @Override
    public MultiTagContainer addAll(@Nullable TagContainer<?> tags, @Nullable FormatPrefix prefix) {
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
    public MultiTagContainer addAll(@Nullable MergedTagContainer container){
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
