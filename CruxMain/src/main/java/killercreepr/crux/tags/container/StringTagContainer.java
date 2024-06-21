package killercreepr.crux.tags.container;

import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.resolver.StringResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringTagContainer extends SimpleTagContainer<StringResolver> {
    public StringTagContainer(@NotNull TagParser format) {
        super(format);
    }

    @Override
    public StringTagContainer hook(@Nullable Object info) {
        if(info==null) return this;
        addAll(format.buildStringTags(info));
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
