package killercreepr.crux.tags.hook.impl;

import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.HookedObjectTag;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.resolver.StringResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringHookedObjectTag<T> implements HookedObjectTag<T, StringResolver> {
    protected final @NotNull T object;
    protected final @NotNull ObjectTag<T> tagProvider;
    protected final @NotNull TagContainer<StringResolver> tags;
    protected final @Nullable FormatPrefix prefix;

    public StringHookedObjectTag(@NotNull T object, @NotNull ObjectTag<T> tagProvider, @NotNull TagContainer<StringResolver> tags, @Nullable FormatPrefix prefix) {
        this.object = object;
        this.tagProvider = tagProvider;
        this.tags = tags;
        this.prefix = prefix;
    }

    public @NotNull ObjectTag<T> getTagProvider() {
        return tagProvider;
    }

    public @Nullable FormatPrefix getPrefix() {
        return prefix;
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
    public @NotNull TagContainer<StringResolver> getTags() {
        return tags;
    }
}
