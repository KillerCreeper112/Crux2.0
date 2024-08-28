package killercreepr.crux.tags.container;

import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.TagsPrefixBuilder;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.resolver.StringResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class SimpleStringTagProvider implements StringTagContainProvider {
    protected final @NotNull TagParser tags;
    protected final @NotNull TagContainer<StringResolver> strings;

    public SimpleStringTagProvider(@NotNull TagParser tags, @NotNull TagContainer<StringResolver> strings) {
        this.tags = tags;
        this.strings = strings;
    }

    public SimpleStringTagProvider(@NotNull TagParser tags) {
        this(tags, TagContainer.string(tags));
    }

    @Override
    public StringTagContainProvider hook(@Nullable Object info) {
        strings.hook(info);
        return this;
    }

    @Override
    public StringTagContainProvider hook(@Nullable Object info, @Nullable TagsPrefixBuilder prefix) {
        strings.hook(info, prefix);
        return this;
    }

    @Override
    public StringTagContainProvider add(@NotNull StringResolver resolver) {
        strings.add(resolver);
        return this;
    }

    @Override
    public StringTagContainProvider add(@NotNull StringResolver resolver, @Nullable FormatPrefix prefix) {
        strings.add(resolver);
        return this;
    }

    @Override
    public StringTagContainProvider addAll(@Nullable TagContainer<StringResolver> tags) {
        strings.addAll(tags);
        return this;
    }

    @Override
    public StringTagContainProvider addAll(@Nullable Collection<StringResolver> tags) {
        strings.addAll(tags);
        return this;
    }

    @Override
    public StringTagContainProvider addAll(@Nullable Collection<StringResolver> tags, @Nullable FormatPrefix prefix) {
        strings.addAll(tags, prefix);
        return this;
    }

    @Override
    public StringTagContainProvider addExact(@NotNull StringResolver resolver, @NotNull String id) {
        strings.add(resolver);
        return this;
    }

    @Override
    public StringTagContainProvider addAll(@Nullable TagContainer<StringResolver> tags, @Nullable FormatPrefix prefix) {
        strings.addAll(tags, prefix);
        return this;
    }

    @Override
    public @Nullable StringResolver get(@NotNull String id) {
        return strings.get(id);
    }

    @Override
    public @NotNull Map<String, StringResolver> asMap() {
        return strings.asMap();
    }

    @Override
    public @NotNull TagParser getTagParser() {
        return tags;
    }

    @NotNull
    @Override
    public Iterator<StringResolver> iterator() {
        return strings.iterator();
    }

    @Override
    public @NotNull TagContainer<StringResolver> getStringTags() {
        return strings;
    }
}
