package killercreepr.crux.tags.hook.impl;

import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.HookedObjectTag;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.resolver.StringListResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringListHookedObjectTag<T> implements HookedObjectTag<T, StringListResolver> {
    protected final @NotNull T object;
    protected final @NotNull ObjectTag<T> tagProvider;
    protected final @NotNull TagContainer<StringListResolver> tags;
    protected final @Nullable FormatPrefix prefix;

    public StringListHookedObjectTag(@NotNull T object, @NotNull ObjectTag<T> tagProvider, @NotNull TagContainer<StringListResolver> tags, @Nullable FormatPrefix prefix) {
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
    public @Nullable FormatPrefix getPrefix() {
        return prefix;
    }
}
