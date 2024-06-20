package killercreepr.crux.tags.ab.container;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.ab.resolver.StringResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StringTagContainer implements TagContainer<StringResolver> {
    protected final Map<String, StringResolver> tags = new HashMap<>();
    @Override
    public StringTagContainer hook(@Nullable Object info) {
        return null;
    }

    @Override
    public StringTagContainer hook(@NotNull TextParserContext context, @Nullable Object info) {
        return null;
    }

    @Override
    public StringTagContainer add(@NotNull StringResolver resolver) {
        tags.put(resolver.identifier(), resolver);
        return this;
    }

    @NotNull
    @Override
    public Iterator<StringResolver> iterator() {
        return tags.values().iterator();
    }
}
