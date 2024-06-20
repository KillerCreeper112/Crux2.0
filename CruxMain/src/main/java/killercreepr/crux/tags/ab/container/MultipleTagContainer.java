package killercreepr.crux.tags.ab.container;

import killercreepr.crux.tags.ab.context.FormatPrefix;
import killercreepr.crux.tags.ab.resolver.StringListResolver;
import killercreepr.crux.tags.ab.resolver.StringResolver;
import killercreepr.crux.tags.ab.tags.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//todo better naming
public class MultipleTagContainer implements MultiTagContainer {
    protected final Tags tags;
    protected final @NotNull StringTagContainer strings;
    protected final @NotNull StringListTagContainer stringLists;
    public MultipleTagContainer(Tags tags){
        this(tags, new StringTagContainer(tags), new StringListTagContainer(tags));
    }
    public MultipleTagContainer(Tags tags, @NotNull StringTagContainer strings, @NotNull StringListTagContainer stringLists) {
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

    public MultipleTagContainer hook(@Nullable Object info) {
        strings.hook(info);
        stringLists.hook(info);
        return this;
    }

    //convenience methods
    //strings
    public MultipleTagContainer add(@NotNull StringResolver resolver) {
        strings.hook(resolver);
        stringLists.hook(resolver);
        return this;
    }

    public MultipleTagContainer add(@NotNull StringResolver resolver, @Nullable FormatPrefix prefix) {
        strings.add(resolver, prefix);
        return this;
    }

    public MultipleTagContainer addAll(@Nullable TagContainer<?> tags) {
        return addAll(tags, null);
    }

    public MultipleTagContainer addAll(@Nullable TagContainer<?> tags, @Nullable FormatPrefix prefix) {
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

    //string lists
    public MultipleTagContainer add(@NotNull StringListResolver resolver) {
        strings.hook(resolver);
        stringLists.hook(resolver);
        return this;
    }

    public MultipleTagContainer add(@NotNull StringListResolver resolver, @Nullable FormatPrefix prefix) {
        stringLists.add(resolver, prefix);
        return this;
    }

    public MultipleTagContainer add(@NotNull MultipleTagContainer container){
        strings.addAll(container.getStringTags());
        stringLists.addAll(container.getStringListTags());
        return this;
    }
}
