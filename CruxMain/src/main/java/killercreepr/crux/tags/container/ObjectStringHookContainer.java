package killercreepr.crux.tags.container;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.tags.format.FormatPrefix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

//todo just make generally better naming
public class ObjectStringHookContainer extends StringHookContainer implements IObjectTagResolverContainer{
    private final Map<String, PrefixBuilder> prefixBuilders = new HashMap<>();
    public ObjectStringHookContainer(@NotNull TextParserContext context, @Nullable StringHookContainer container) {
        super(context);
        if(container instanceof ObjectStringHookContainer c){
            prefixBuilders.putAll(c.getPrefixBuilders());
        }
    }

    public ObjectStringHookContainer(@NotNull StringHookContainer container) {
        super(container);
    }

    public @NotNull TextParserContext getContext() {
        return context;
    }

    public ObjectStringHookContainer(@NotNull TextParserContext context) {
        super(context);
    }
    @Override
    public ObjectStringHookContainer mergePrefixBuilders(@NotNull Map<String, PrefixBuilder> prefixBuilders){
        this.prefixBuilders.putAll(prefixBuilders);
        return this;
    }
    @Override
    public ObjectStringHookContainer setPrefixBuilder(@NotNull String id, @NotNull PrefixBuilder prefix){
        prefixBuilders.put(id, prefix);
        return this;
    }
    @Override
    public ObjectStringHookContainer hookAll(@NotNull DataExchange info){
        return hookAll(context, info, prefixBuilders);
    }
    @Override
    public ObjectStringHookContainer hookAll(@NotNull TextParserContext context,  @NotNull DataExchange info){
        return hookAll(context, info, prefixBuilders);
    }
    @Override
    public ObjectStringHookContainer hookAll(@NotNull DataExchange info, @Nullable Map<String, PrefixBuilder> prefix){
        return hookAll(context, info, prefix);
    }
    @Override
    public ObjectStringHookContainer hookAll(@NotNull TextParserContext context, @NotNull DataExchange info, @Nullable Map<String, PrefixBuilder> prefixBuilders){
        if(prefixBuilders == null){
            putAll(context.getFormat().getTags().hookAllTagResolvers(context, info));
            return this;
        }
        StringHookContainer container = new StringHookContainer(getContext());
        info.getData().forEach((id, value) ->{
            if(value == null) return;
            PrefixBuilder prefix = prefixBuilders.getOrDefault(id, null);
            container.putAll(context.getFormat().getTags().hookStringResolvers(context, value, prefix == null ? null : prefix.build(info, id, value)));
        });
        putAll(container);
        return this;
    }
    @Override
    public ObjectStringHookContainer hook(@Nullable Object info){
        return hook(context, info);
    }

    @Override
    public ObjectStringHookContainer hook(@NotNull TextParserContext context, @Nullable Object info){
        return hook(context, info, null);
    }

    public ObjectStringHookContainer hook(@Nullable Object info, @Nullable FormatPrefix prefix){
        return hook(context, info, prefix);
    }

    public ObjectStringHookContainer hook(@NotNull TextParserContext context, @Nullable Object info, @Nullable FormatPrefix prefix){
        if(info == null) return this;
        putAll(context.getFormat().getTags().hookStringResolvers(context, Holder.directObject(info), prefix));
        return this;
    }

    public boolean isEmpty(){
        return prefixBuilders.isEmpty();
    }

    @NotNull
    @Override
    public Map<String, PrefixBuilder> getPrefixBuilders() {
        return prefixBuilders;
    }
}
