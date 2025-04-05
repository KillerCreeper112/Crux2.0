package killercreepr.crux.core.text.container;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.TagsPrefixBuilder;
import killercreepr.crux.api.text.tags.container.StringTagContainProvider;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

public class SimpleStringTagProvider implements StringTagContainProvider {
    protected final @NotNull TagParser tags;
    protected final @NotNull TagContainer<StringResolver> strings;

    public SimpleStringTagProvider(@NotNull TagParser tags, @NotNull TagContainer<StringResolver> strings) {
        this.tags = tags;
        this.strings = strings;
    }

    public SimpleStringTagProvider(@NotNull TagParser tags) {
        this(tags, new StringTagContainer(tags));
    }

    @Override
    public String toString() {
        return "SimpleStringTagProvider{tags=" + tags + ", strings=" + strings + "}";
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
    public StringTagContainProvider hookAllWithPrefix(@Nullable DataExchange info, @Nullable Function<String, TagsPrefixBuilder> prefixConsumer) {
        if(info==null) return this;
        info.forEach((id, holder) ->{
            Object value = holder.value();
            if(id.isBlank() || !info.isExplicitlySet(id)){
                hook(value);
                return;
            }
            TagsPrefixBuilder prefix = prefixConsumer == null ? TagsPrefixBuilder.overwriteBase(id + "_") : prefixConsumer.apply(id);

            if(isPrimitive(value)) add(Tag.parsed(id, value.toString()));

            hook(value, prefix);
        });
        return this;
    }

    private boolean isPrimitive(Object o){
        return o instanceof String || o instanceof Number || o instanceof Boolean || o instanceof Character;
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
