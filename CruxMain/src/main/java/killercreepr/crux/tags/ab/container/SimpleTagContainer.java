package killercreepr.crux.tags.ab.container;

import killercreepr.crux.tags.ab.context.FormatPrefix;
import killercreepr.crux.tags.ab.resolver.TagResolver;
import killercreepr.crux.tags.ab.tags.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class SimpleTagContainer<T extends TagResolver<?>> implements TagContainer<T> {
    protected final @NotNull Tags format;
    protected final Map<String, T> tags = new HashMap<>();
    public SimpleTagContainer(@NotNull Tags format) {
        this.format = format;
    }
    @Override
    public SimpleTagContainer<T>  add(@NotNull T resolver) {
        tags.put(resolver.identifier(), resolver);
        return this;
    }

    @Override
    public SimpleTagContainer<T>  add(@NotNull T resolver, @Nullable FormatPrefix prefix) {
        if(prefix==null){
            tags.put(resolver.identifier(), resolver);
            return this;
        }
        tags.put(prefix.buildPrefix(resolver) + resolver.identifier(), resolver);
        return this;
    }

    @Override
    public SimpleTagContainer<T>  addAll(@Nullable TagContainer<T> tags) {
        if(tags==null) return this;
        tags.forEach(this::add);
        return this;
    }

    @Override
    public SimpleTagContainer<T> addAll(@Nullable TagContainer<T> tags, @Nullable FormatPrefix prefix) {
        if(tags==null) return this;
        tags.forEach(x -> add(x, prefix));
        return this;
    }

    @Override
    public @Nullable T get(@NotNull String id) {
        return tags.get(id);
    }

    @Override
    public @NotNull Map<String, T> asMap() {
        return tags;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return tags.values().iterator();
    }
}
