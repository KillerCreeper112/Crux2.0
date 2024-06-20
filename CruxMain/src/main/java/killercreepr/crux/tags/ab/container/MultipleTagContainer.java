package killercreepr.crux.tags.ab.container;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.tags.ab.StringListTagProvider;
import killercreepr.crux.tags.ab.StringTagProvider;
import killercreepr.crux.tags.ab.resolver.StringResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

//todo better naming
public class MultipleTagContainer implements TagContainer<StringResolver>, StringTagProvider, StringListTagProvider {
    protected final @NotNull StringTagContainer strings;
    protected final @NotNull StringListTagContainer stringLists;
    public MultipleTagContainer(){
        this(new StringTagContainer(), new StringListTagContainer());
    }
    public MultipleTagContainer(@NotNull StringTagContainer strings, @NotNull StringListTagContainer stringLists) {
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
    public MultipleTagContainer hook(@Nullable Object info) {
        strings.hook(info);
        stringLists.hook(info);
        return this;
    }

    @Override
    public MultipleTagContainer hook(@NotNull TextParserContext context, @Nullable Object info) {
        strings.hook(context, info);
        stringLists.hook(context, info);
        return this;
    }

    @Override
    public MultipleTagContainer add(@NotNull StringResolver resolver) {
        strings.hook(resolver);
        stringLists.hook(resolver);
        return this;
    }

    public MultipleTagContainer add(@NotNull MultipleTagContainer container){
        //todo
        return this;
    }

    @NotNull
    @Override
    public Iterator<StringResolver> iterator() {
        return strings.iterator();
    }
}
