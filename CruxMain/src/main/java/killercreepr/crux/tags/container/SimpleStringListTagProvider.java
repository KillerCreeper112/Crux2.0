package killercreepr.crux.tags.container;

import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.TagsPrefixBuilder;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.resolver.StringListResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class SimpleStringListTagProvider implements StringListTagContainProvider {
    protected final @NotNull TagParser tags;
    protected final @NotNull TagContainer<StringListResolver> strings;

    public SimpleStringListTagProvider(@NotNull TagParser tags, @NotNull TagContainer<StringListResolver> strings) {
        this.tags = tags;
        this.strings = strings;
    }

    public SimpleStringListTagProvider(@NotNull TagParser tags) {
        this(tags, TagContainer.stringList(tags));
    }

    @Override
    public StringListTagContainProvider hook(@Nullable Object info) {
        strings.hook(info);
        return this;
    }

    @Override
    public StringListTagContainProvider hook(@Nullable Object info, @Nullable TagsPrefixBuilder prefix) {
        strings.hook(info, prefix);
        return this;
    }

    @Override
    public StringListTagContainProvider add(@NotNull StringListResolver resolver) {
        strings.add(resolver);
        return this;
    }

    @Override
    public StringListTagContainProvider add(@NotNull StringListResolver resolver, @Nullable FormatPrefix prefix) {
        strings.add(resolver);
        return this;
    }

    @Override
    public StringListTagContainProvider addAll(@Nullable TagContainer<StringListResolver> tags) {
        strings.addAll(tags);
        return this;
    }

    @Override
    public StringListTagContainProvider addAll(@Nullable Collection<StringListResolver> tags) {
        strings.addAll(tags);
        return this;
    }

    @Override
    public StringListTagContainProvider addAll(@Nullable Collection<StringListResolver> tags, @Nullable FormatPrefix prefix) {
        strings.addAll(tags, prefix);
        return this;
    }

    @Override
    public StringListTagContainProvider addExact(@NotNull StringListResolver resolver, @NotNull String id) {
        strings.add(resolver);
        return this;
    }

    @Override
    public StringListTagContainProvider addAll(@Nullable TagContainer<StringListResolver> tags, @Nullable FormatPrefix prefix) {
        strings.addAll(tags, prefix);
        return this;
    }

    @Override
    public @Nullable StringListResolver get(@NotNull String id) {
        return strings.get(id);
    }

    @Override
    public @NotNull Map<String, StringListResolver> asMap() {
        return strings.asMap();
    }

    @Override
    public @NotNull TagParser getTagParser() {
        return tags;
    }

    @NotNull
    @Override
    public Iterator<StringListResolver> iterator() {
        return strings.iterator();
    }

    @Override
    public @NotNull TagContainer<StringListResolver> getStringListTags() {
        return strings;
    }
}
