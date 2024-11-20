package killercreepr.crux.core.text.hook;

import killercreepr.crux.api.text.hook.HookedObjectTag;
import killercreepr.crux.api.text.hook.HookedPrefixBuilder;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.container.TagContainer;
import org.jetbrains.annotations.NotNull;

public class StringHookedObjectTag<T> implements HookedObjectTag<T, StringResolver> {
    protected final @NotNull T object;
    protected final @NotNull ObjectTag<T> tagProvider;
    protected final @NotNull TagContainer<StringResolver> tags;
    protected final @NotNull HookedPrefixBuilder prefix;

    public StringHookedObjectTag(@NotNull T object, @NotNull ObjectTag<T> tagProvider, @NotNull TagContainer<StringResolver> tags, @NotNull HookedPrefixBuilder prefix) {
        this.object = object;
        this.tagProvider = tagProvider;
        this.tags = tags;
        this.prefix = prefix;
    }

    public StringHookedObjectTag<T> withAddedPrefix(@NotNull HookedPrefixBuilder prefix){
        HookedPrefixBuilder result = HookedPrefixBuilder.add(prefix, this.prefix);
        return new StringHookedObjectTag<>(object, tagProvider, tags, result);
    }

    public @NotNull ObjectTag<T> getTagProvider() {
        return tagProvider;
    }

    public @NotNull HookedPrefixBuilder getPrefix() {
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
