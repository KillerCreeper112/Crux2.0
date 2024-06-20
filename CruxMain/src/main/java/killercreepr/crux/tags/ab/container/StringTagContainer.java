package killercreepr.crux.tags.ab.container;

import killercreepr.crux.tags.ab.resolver.StringResolver;
import killercreepr.crux.tags.ab.tags.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringTagContainer extends SimpleTagContainer<StringResolver> {
    public StringTagContainer(@NotNull Tags format) {
        super(format);
    }

    @Override
    public SimpleTagContainer<StringResolver> hook(@Nullable Object info) {
        if(info==null) return this;
        return addAll(format.buildStringTags(info));
    }
}
