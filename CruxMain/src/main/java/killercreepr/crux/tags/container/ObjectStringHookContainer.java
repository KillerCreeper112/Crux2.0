package killercreepr.crux.tags.container;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.tags.Tags;
import killercreepr.crux.tags.format.FormatPrefix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ObjectStringHookContainer extends StringHookContainer implements IObjectTagResolverContainer{
    private final @NotNull Tags tags;
    private final Map<String, PrefixBuilder> prefixBuilders = new HashMap<>();
    public ObjectStringHookContainer(@Nullable StringHookContainer container, @NotNull Tags tags) {
        super(container);
        this.tags = tags;
        if(container instanceof ObjectStringHookContainer c){
            prefixBuilders.putAll(c.getPrefixBuilders());
        }
    }

    public ObjectStringHookContainer(@NotNull ObjectStringHookContainer x) {
        this(x, x.getTags());
    }
    public ObjectStringHookContainer(@NotNull Tags tags) {
        this.tags = tags;
    }

    public ObjectStringHookContainer mergePrefixBuilders(@NotNull Map<String, PrefixBuilder> prefixBuilders){
        this.prefixBuilders.putAll(prefixBuilders);
        return this;
    }

    public ObjectStringHookContainer setPrefixBuilder(@NotNull String id, @NotNull PrefixBuilder prefix){
        prefixBuilders.put(id, prefix);
        return this;
    }

    public ObjectStringHookContainer hookAll(@NotNull DataExchange info){
        return hookAll(info, tags, prefixBuilders);
    }

    public ObjectStringHookContainer hookAll(@NotNull DataExchange info, @NotNull Tags tags){
        return hookAll(info, tags, prefixBuilders);
    }

    public ObjectStringHookContainer hookAll(@NotNull DataExchange info, @Nullable Map<String, PrefixBuilder> prefix){
        return hookAll(info, tags, prefix);
    }

    public ObjectStringHookContainer hookAll(@NotNull DataExchange info, @NotNull Tags tags, @Nullable Map<String, PrefixBuilder> prefixBuilders){
        if(prefixBuilders == null){
            putAll(tags.hookAllTagResolvers(info));
            return this;
        }
        StringHookContainer container = new StringHookContainer();
        info.getData().forEach((id, value) ->{
            if(value == null) return;
            PrefixBuilder prefix = prefixBuilders.getOrDefault(id, null);
            container.putAll(tags.hookStringResolvers(value, prefix == null ? null : prefix.build(info, id, value)));
        });
        putAll(container);
        return this;
    }

    public ObjectStringHookContainer hook(@Nullable Object info){
        return hook(info, tags);
    }

    public ObjectStringHookContainer hook(@Nullable Object info, @NotNull Tags tags){
        return hook(info, tags, null);
    }

    public ObjectStringHookContainer hook(@Nullable Object info, @Nullable FormatPrefix prefix){
        return hook(info, tags, prefix);
    }

    public ObjectStringHookContainer hook(@Nullable Object info, @NotNull Tags tags, @Nullable FormatPrefix prefix){
        if(info == null) return this;
        putAll(tags.hookStringResolvers(Holder.directObject(info), prefix));
        return this;
    }

    public @NotNull Tags getTags() {
        return tags;
    }

    @NotNull
    @Override
    public Map<String, PrefixBuilder> getPrefixBuilders() {
        return prefixBuilders;
    }
}
