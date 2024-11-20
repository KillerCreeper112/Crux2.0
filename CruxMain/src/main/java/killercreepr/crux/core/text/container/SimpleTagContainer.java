package killercreepr.crux.core.text.container;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.resolver.TagResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class SimpleTagContainer<T extends TagResolver<?>> implements TagContainer<T> {
    protected final @NotNull TagParser tagParser;
    protected final Map<String, T> tags = new HashMap<>();
    public SimpleTagContainer(@NotNull TagParser tagParser) {
        this.tagParser = tagParser;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{tagParser=" + tagParser + ", tags=" + tags + "}";
    }

    @Override
    public SimpleTagContainer<T>  add(@NotNull T resolver) {
        tags.put(resolver.identifier(), resolver);
        return this;
    }

    public Map<String, T> getTags() {
        return tags;
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
    public SimpleTagContainer<T> addAll(@Nullable TagContainer<T> tags) {
        if(tags==null) return this;
        tags.asMap().forEach((id, tag) -> addExact(tag, id));
        return this;
    }

    @Override
    public TagContainer<T> addAll(@Nullable Collection<T> tags) {
        if(tags==null) return this;
        tags.forEach(this::add);
        return this;
    }

    @Override
    public TagContainer<T> addAll(@Nullable Collection<T> tags, @Nullable FormatPrefix prefix) {
        if(tags==null) return this;
        //TODO ADD SEPARATE OPTIONS ONE FOR OVERWRITING PREFIX AND ONE FOR ADDING
        tags.forEach(t -> add(t, prefix));
        return this;
    }

    @Override
    public SimpleTagContainer<T> addAll(@Nullable TagContainer<T> tags, @Nullable FormatPrefix prefix) {
        if(tags==null) return this;
        //TODO ADD SEPARATE OPTIONS ONE FOR OVERWRITING PREFIX AND ONE FOR ADDING
        tags.forEach(x -> add(x, prefix));
        return this;
    }

    @Override
    public SimpleTagContainer<T> addExact(@NotNull T resolver, @NotNull String id) {
        tags.put(id, resolver);
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

    @Override
    public @NotNull TagParser getTagParser() {
        return tagParser;
    }
}
