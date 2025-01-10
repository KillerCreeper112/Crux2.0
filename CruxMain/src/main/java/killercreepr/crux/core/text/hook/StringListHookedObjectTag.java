package killercreepr.crux.core.text.hook;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.HookedObjectTag;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringListResolver;
import killercreepr.crux.api.text.tags.container.TagContainer;
import org.jetbrains.annotations.NotNull;

public class StringListHookedObjectTag<T> implements HookedObjectTag<T, StringListResolver> {
    protected final @NotNull Object base;
    protected final @NotNull Object parent;
    protected final @NotNull T object;
    protected final @NotNull ObjectTag<T> tagProvider;
    protected final @NotNull TagContainer<StringListResolver> tags;
    protected final @NotNull FormatPrefix prefix;

    public StringListHookedObjectTag(@NotNull Object base, @NotNull Object parent, @NotNull T object, @NotNull ObjectTag<T> tagProvider, @NotNull TagContainer<StringListResolver> tags, @NotNull FormatPrefix prefix) {
        this.base = base;
        this.parent = parent;
        this.object = object;
        this.tagProvider = tagProvider;
        this.tags = tags;
        this.prefix = prefix;
    }

    @Override
    public @NotNull ObjectTag<T> getObjectTag() {
        return tagProvider;
    }

    @Override
    public @NotNull T getObject() {
        return object;
    }

    @Override
    public @NotNull TagContainer<StringListResolver> getTags() {
        return tags;
    }

    @Override
    public @NotNull FormatPrefix getPrefix() {
        return prefix;
    }
}
