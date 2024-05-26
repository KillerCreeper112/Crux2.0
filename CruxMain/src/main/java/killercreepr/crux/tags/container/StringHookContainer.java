package killercreepr.crux.tags.container;

import killercreepr.crux.tags.FormatContext;
import killercreepr.crux.tags.hook.StringHookedObject;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class StringHookContainer implements TagsContainer<StringHookedObject<?>> {
    protected final @NotNull FormatContext context;
    private final Map<String, StringHookedObject<?>> tags = new HashMap<>();
    public StringHookContainer(@NotNull FormatContext context, @Nullable StringHookContainer container){
        this.context = context;
        this.putAll(container);
    }

    public StringHookContainer(@NotNull StringHookContainer container){
        this(container.getContext(), container);
    }

    public StringHookContainer(@NotNull FormatContext context){
        this.context = context;
    }

    public Map<String, StringHookedObject<?>> getTags() {
        return tags;
    }

    public @NotNull FormatContext getContext() {
        return context;
    }

    @Override
    public @NotNull StringHookContainer clone(){
        return new StringHookContainer(context, this);
    }

    public StringHookContainer put(@NotNull StringHookedObject<?> hooked){
        tags.put(hooked.identifier(), hooked);
        return this;
    }

    public @NotNull TagResolver[] buildTagResolvers(){
        Collection<TagResolver> list = new HashSet<>();
        tags.values().forEach(hooked ->{
            TagResolver resolver = hooked.tagResolver(context);
            if(resolver != null) list.add(resolver);
        });
        return list.toArray(new TagResolver[0]);
    }

    public StringHookContainer putAll(@Nullable StringHookContainer container){
        if(container != null) tags.putAll(container.get());
        return this;
    }

    @Override
    public StringHookContainer put(@NotNull String id, @Nullable StringHookedObject<?> tag) {
        if(tag == null) return this;
        tags.put(id, tag);
        return this;
    }

    @Override
    public StringHookContainer putIfAbsent(@NotNull String id, @Nullable StringHookedObject<?> tag) {
        if(tag == null) return this;
        tags.putIfAbsent(id, tag);
        return this;
    }

    @Override
    public StringHookContainer remove(@NotNull String id) {
        tags.remove(id);
        return this;
    }

    @Override
    public boolean has(@NotNull String id) {
        return tags.containsKey(id);
    }

    @Override
    public @Nullable StringHookedObject<?> get(@NotNull String id) {
        return tags.getOrDefault(id, null);
    }

    @Override
    public @NotNull Map<String, StringHookedObject<?>> get() {
        return tags;
    }
}
