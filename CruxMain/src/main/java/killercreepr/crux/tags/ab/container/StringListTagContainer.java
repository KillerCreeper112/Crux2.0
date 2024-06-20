package killercreepr.crux.tags.ab.container;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.ab.resolver.StringListResolver;
import killercreepr.crux.tags.ab.resolver.StringResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StringListTagContainer implements TagContainer<StringListResolver> {
    protected final Map<String, StringListResolver> tags = new HashMap<>();
    @Override
    public StringListTagContainer hook(@Nullable Object info) {
        return null;
    }

    @Override
    public StringListTagContainer hook(@NotNull TextParserContext context, @Nullable Object info) {
        return null;
    }

    @Override
    public StringListTagContainer add(@NotNull StringListResolver resolver) {
        tags.put(resolver.identifier(), resolver);
        return this;
    }

    @NotNull
    @Override
    public Iterator<StringListResolver> iterator() {
        return tags.values().iterator();
    }
}
