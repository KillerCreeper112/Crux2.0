package killercreepr.crux.tags.ab.container;

import killercreepr.crux.tags.ab.resolver.StringListResolver;
import killercreepr.crux.tags.ab.tags.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringListTagContainer extends SimpleTagContainer<StringListResolver> {
    public StringListTagContainer(@NotNull Tags format) {
        super(format);
    }

    @Override
    public SimpleTagContainer<StringListResolver> hook(@Nullable Object info) {
        if(info==null) return this;
        return addAll(format.buildStringListTags(info));
    }
}
