package killercreepr.crux.tags.container;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.tags.format.FormatPrefix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ObjectLoreHookContainer extends LoreHookContainer implements IObjectTagResolverContainer{
    private final @NotNull TextParserContext context;
    private final Map<String, PrefixBuilder> prefixBuilders = new HashMap<>();
    public ObjectLoreHookContainer(@NotNull TextParserContext context, @Nullable LoreHookContainer container) {
        super(container);
        this.context = context;
        if(container instanceof IObjectTagResolverContainer c){
            prefixBuilders.putAll(c.getPrefixBuilders());
        }
    }

    //yeah i get it messy messy man
    public ObjectLoreHookContainer(@NotNull TextParserContext context, @NotNull IObjectTagResolverContainer c) {
        this.context = context;
        prefixBuilders.putAll(c.getPrefixBuilders());
    }

    public ObjectLoreHookContainer(@NotNull TextParserContext context) {
        this.context = context;
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
        return hookAll(context, info, prefixBuilders);
    }

    public ObjectLoreHookContainer hookAll(@NotNull TextParserContext context, @NotNull DataExchange info){
        return hookAll(context, info, prefixBuilders);
    }

    public ObjectLoreHookContainer hookAll(@NotNull DataExchange info, @Nullable Map<String, PrefixBuilder> prefix){
        return hookAll(context, info, prefix);
    }

    public ObjectLoreHookContainer hookAll(@NotNull TextParserContext context, @NotNull DataExchange info, @Nullable Map<String, PrefixBuilder> prefixBuilders){
        if(prefixBuilders == null){
            putAll(context.getFormat().getTags().hookAllLoreTags(info));
            return this;
        }
        LoreHookContainer container = new LoreHookContainer();
        info.getData().forEach((id, value) ->{
            if(value == null) return;
            PrefixBuilder prefix = prefixBuilders.getOrDefault(id, null);
            container.putAll(context.getFormat().getTags().hookLoreTags(value, prefix == null ? null : prefix.build(info, id, value)));
        });
        putAll(container);
        return this;
    }

    public ObjectLoreHookContainer hook(@Nullable Object info){
        return hook(context, info);
    }

    public ObjectLoreHookContainer hook(@NotNull TextParserContext context, @Nullable Object info){
        return hook(context, info, null);
    }

    public ObjectLoreHookContainer hook(@NotNull TextParserContext context, @Nullable Object info, @Nullable FormatPrefix prefix){
        return hook(context, info, prefix);
    }

    public ObjectLoreHookContainer hook(@Nullable Object info, @NotNull TextParserContext tags, @Nullable FormatPrefix prefix){
        if(info == null) return this;
        putAll(tags.getFormat().getTags().hookLoreTags(info, prefix));
        return this;
    }

    public @NotNull TextParserContext getContext() {
        return context;
    }

    @NotNull
    @Override
    public Map<String, PrefixBuilder> getPrefixBuilders() {
        return prefixBuilders;
    }
}
