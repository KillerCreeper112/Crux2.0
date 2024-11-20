package killercreepr.crux.core.text.container;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.resolver.StringListResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.TagsPrefixBuilder;
import killercreepr.crux.api.text.tags.container.TagContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringListTagContainer extends SimpleTagContainer<StringListResolver> {
    public StringListTagContainer(@NotNull TagParser format) {
        super(format);
    }

    @Override
    public SimpleTagContainer<StringListResolver> hook(@Nullable Object info) {
        if(info==null) return this;
        return addAll(tagParser.buildStringListTags(info));
    }

    @Override
    public TagContainer<StringListResolver> hook(@Nullable Object info, @Nullable TagsPrefixBuilder prefix) {
        if(info==null) return this;
        return addAll(tagParser.buildStringListTags(info, prefix));
    }

    @Override
    public StringListTagContainer add(@NotNull StringListResolver resolver) {
        super.add(resolver);
        return this;
    }

    @Override
    public StringListTagContainer add(@NotNull StringListResolver resolver, @Nullable FormatPrefix prefix) {
        super.add(resolver, prefix);
        return this;
    }

    @Override
    public StringListTagContainer addAll(@Nullable TagContainer<StringListResolver> tags) {
        super.addAll(tags);
        return this;
    }

    @Override
    public StringListTagContainer addAll(@Nullable TagContainer<StringListResolver> tags, @Nullable FormatPrefix prefix) {
        super.addAll(tags, prefix);
        return this;
    }
}
