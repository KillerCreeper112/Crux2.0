package killercreepr.crux.tags.ab.container;

import killercreepr.crux.tags.ab.context.FormatPrefix;
import killercreepr.crux.tags.ab.resolver.StringListResolver;
import killercreepr.crux.tags.ab.tags.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StringListTagContainer implements TagContainer<StringListResolver> {
    protected final @NotNull Tags format;

    public StringListTagContainer(@NotNull Tags format) {
        this.format = format;
    }

    protected final Map<String, StringListResolver> tags = new HashMap<>();
    @Override
    public StringListTagContainer hook(@Nullable Object info) {
        if(info==null) return this;
        addAll(format.buildStringListTags(info));
        return this;
    }
    @Override
    public StringListTagContainer add(@NotNull StringListResolver resolver) {
        tags.put(resolver.identifier(), resolver);
        return this;
    }

    @Override
    public TagContainer<StringListResolver> add(@NotNull StringListResolver resolver, @Nullable FormatPrefix prefix) {
        if(prefix==null){
            tags.put(resolver.identifier(), resolver);
            return this;
        }
        tags.put(prefix.buildPrefix(resolver) + resolver.identifier(), resolver);
        return this;
    }

    @Override
    public StringListTagContainer addAll(@Nullable TagContainer<StringListResolver> tags) {
        if(tags==null) return this;
        tags.forEach(this::add);
        return this;
    }

    @Override
    public StringListTagContainer addAll(@Nullable TagContainer<StringListResolver> tags, @Nullable FormatPrefix prefix) {
        if(tags==null) return this;
        tags.forEach(x -> add(x, prefix));
        return this;
    }

    @Override
    public @Nullable StringListResolver get(@NotNull String id) {
        return tags.get(id);
    }

    @Override
    public @NotNull Map<String, StringListResolver> asMap() {
        return tags;
    }

    @NotNull
    @Override
    public Iterator<StringListResolver> iterator() {
        return tags.values().iterator();
    }
}
