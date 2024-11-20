package killercreepr.crux.core.text.container;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.TagsPrefixBuilder;
import killercreepr.crux.api.text.tags.container.TagContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringTagContainer extends SimpleTagContainer<StringResolver> {
    public StringTagContainer(@NotNull TagParser format) {
        super(format);
    }

    @Override
    public StringTagContainer hook(@Nullable Object info) {
        if(info==null) return this;
        addAll(tagParser.buildStringTags(info));
        return this;
    }

    @Override
    public StringTagContainer hook(@Nullable Object info, @Nullable TagsPrefixBuilder prefix) {
        if(info==null) return this;
        addAll(tagParser.buildStringTags(info, prefix));
        return this;
    }

    @Override
    public StringTagContainer add(@NotNull StringResolver resolver) {
        super.add(resolver);
        return this;
    }

    @Override
    public StringTagContainer add(@NotNull StringResolver resolver, @Nullable FormatPrefix prefix) {
        super.add(resolver, prefix);
        return this;
    }

    @Override
    public StringTagContainer addAll(@Nullable TagContainer<StringResolver> tags) {
        super.addAll(tags);
        return this;
    }

    @Override
    public StringTagContainer addAll(@Nullable TagContainer<StringResolver> tags, @Nullable FormatPrefix prefix) {
        super.addAll(tags, prefix);
        return this;
    }
}
