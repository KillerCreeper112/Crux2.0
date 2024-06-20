package killercreepr.crux.tags.ab.container;

import killercreepr.crux.tags.ab.context.FormatPrefix;
import killercreepr.crux.tags.ab.resolver.StringResolver;
import killercreepr.crux.tags.ab.tags.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StringTagContainer implements TagContainer<StringResolver> {
    protected final @NotNull Tags format;
    protected final Map<String, StringResolver> tags = new HashMap<>();

    public StringTagContainer(@NotNull Tags tags) {
        this.format = tags;
    }

    @Override
    public StringTagContainer hook(@Nullable Object info) {
        if(info==null) return this;
        return addAll(format.buildStringTags(info));
    }

    @Override
    public StringTagContainer add(@NotNull StringResolver resolver) {
        return add(resolver, null);
    }

    @Override
    public StringTagContainer add(@NotNull StringResolver resolver, @Nullable FormatPrefix prefix) {
        if(prefix==null){
            tags.put(resolver.identifier(), resolver);
            return this;
        }
        tags.put(prefix.buildPrefix(resolver) + resolver.identifier(), resolver);
        return this;
    }

    @Override
    public StringTagContainer addAll(@Nullable TagContainer<StringResolver> tags) {
        if(tags==null) return this;
        this.tags.putAll(tags.asMap());
        return this;
    }

    public StringTagContainer addAll(@Nullable TagContainer<StringResolver> tags, @Nullable FormatPrefix prefix) {
        if(tags==null) return this;
        tags.forEach(x -> add(x, prefix));
        return this;
    }

    @Override
    public @Nullable StringResolver get(@NotNull String id) {
        return tags.get(id);
    }

    @Override
    public @NotNull Map<String, StringResolver> asMap() {
        return tags;
    }

    @NotNull
    @Override
    public Iterator<StringResolver> iterator() {
        return tags.values().iterator();
    }
}
