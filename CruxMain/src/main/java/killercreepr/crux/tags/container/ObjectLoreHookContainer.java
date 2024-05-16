package killercreepr.crux.tags.container;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.tags.Tags;
import killercreepr.crux.tags.format.FormatPrefix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ObjectLoreHookContainer extends LoreHookContainer implements IObjectTagResolverContainer{
    private final @NotNull Tags tags;
    private final Map<String, PrefixBuilder> prefixBuilders = new HashMap<>();
    public ObjectLoreHookContainer(@Nullable LoreHookContainer container, @NotNull Tags tags) {
        super(container);
        this.tags = tags;
        if(container instanceof IObjectTagResolverContainer c){
            prefixBuilders.putAll(c.getPrefixBuilders());
        }
    }

    //yeah i get it messy messy man
    public ObjectLoreHookContainer(@NotNull Tags tags, @NotNull IObjectTagResolverContainer c) {
        this.tags = tags;
        prefixBuilders.putAll(c.getPrefixBuilders());
    }

    public ObjectLoreHookContainer(@NotNull Tags tags) {
        this.tags = tags;
    }

    public ObjectLoreHookContainer mergePrefixBuilders(@NotNull Map<String, PrefixBuilder> prefixBuilders){
        this.prefixBuilders.putAll(prefixBuilders);
        return this;
    }

    public ObjectLoreHookContainer setPrefixBuilder(@NotNull String id, @NotNull PrefixBuilder prefix){
        prefixBuilders.put(id, prefix);
        return this;
    }

    public ObjectLoreHookContainer hookAll(@NotNull DataExchange info){
        return hookAll(info, tags, prefixBuilders);
    }

    public ObjectLoreHookContainer hookAll(@NotNull DataExchange info, @NotNull Tags tags){
        return hookAll(info, tags, prefixBuilders);
    }

    public ObjectLoreHookContainer hookAll(@NotNull DataExchange info, @Nullable Map<String, PrefixBuilder> prefix){
        return hookAll(info, tags, prefix);
    }

    public ObjectLoreHookContainer hookAll(@NotNull DataExchange info, @NotNull Tags tags, @Nullable Map<String, PrefixBuilder> prefixBuilders){
        if(prefixBuilders == null){
            putAll(tags.hookAllLoreTags(info));
            return this;
        }
        LoreHookContainer container = new LoreHookContainer();
        info.getData().forEach((id, value) ->{
            if(value == null) return;
            PrefixBuilder prefix = prefixBuilders.getOrDefault(id, null);
            container.putAll(tags.hookLoreTags(value, prefix == null ? null : prefix.build(info, id, value)));
        });
        putAll(container);
        return this;
    }

    public ObjectLoreHookContainer hook(@Nullable Object info){
        return hook(info, tags);
    }

    public ObjectLoreHookContainer hook(@Nullable Object info, @NotNull Tags tags){
        return hook(info, tags, null);
    }

    public ObjectLoreHookContainer hook(@Nullable Object info, @Nullable FormatPrefix prefix){
        return hook(info, tags, prefix);
    }

    public ObjectLoreHookContainer hook(@Nullable Object info, @NotNull Tags tags, @Nullable FormatPrefix prefix){
        if(info == null) return this;
        putAll(tags.hookLoreTags(info, prefix));
        return this;
    }

    public Tags getTags() {
        return tags;
    }

    @NotNull
    @Override
    public Map<String, PrefixBuilder> getPrefixBuilders() {
        return prefixBuilders;
    }
}
