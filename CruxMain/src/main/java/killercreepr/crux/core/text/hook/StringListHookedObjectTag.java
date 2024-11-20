package killercreepr.crux.core.text.hook;

import killercreepr.crux.api.text.hook.HookedObjectTag;
import killercreepr.crux.api.text.hook.HookedPrefixBuilder;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringListResolver;
import killercreepr.crux.api.text.tags.container.TagContainer;
import org.jetbrains.annotations.NotNull;

public class StringListHookedObjectTag<T> implements HookedObjectTag<T, StringListResolver> {
    protected final @NotNull T object;
    protected final @NotNull ObjectTag<T> tagProvider;
    protected final @NotNull TagContainer<StringListResolver> tags;
    protected final @NotNull HookedPrefixBuilder prefix;

    public StringListHookedObjectTag(@NotNull T object, @NotNull ObjectTag<T> tagProvider, @NotNull TagContainer<StringListResolver> tags, @NotNull HookedPrefixBuilder prefix) {
        this.object = object;
        this.tagProvider = tagProvider;
        this.tags = tags;
        this.prefix = prefix;
    }

    public StringListHookedObjectTag<T> withAddedPrefix(@NotNull HookedPrefixBuilder prefix){
        HookedPrefixBuilder result = HookedPrefixBuilder.add(prefix, this.prefix);
        return new StringListHookedObjectTag<>(object, tagProvider, tags, result);
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
    public @NotNull HookedPrefixBuilder getPrefix() {
        return prefix;
    }
}
