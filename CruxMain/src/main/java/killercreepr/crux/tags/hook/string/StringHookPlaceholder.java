package killercreepr.crux.tags.hook.string;

import killercreepr.crux.Crux;
import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.data.Holder;
import killercreepr.crux.tags.FormatArgs;
import killercreepr.crux.tags.Tags;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringHookPlaceholder extends StringHookedObject<Object>{
    public static @NotNull StringHookPlaceholder parsed(@NotNull String id, @NotNull String value){
        return new StringHookPlaceholder(Crux.TAGS, "", StringHook.parsed(id, value));
    }

    public StringHookPlaceholder(@NotNull Tags tags, @NotNull String prefix, @NotNull StringHook<Object> hook) {
        super(tags, prefix, Holder.emptyObject(), hook);
    }

    @Override
    public @Nullable String request(@NotNull FormatArgs args, @NotNull TextParserContext context){
        return hook.parseObject(holder.value(), args, context);
    }
    @Override
    public @Nullable TagResolver tagResolver(@NotNull TextParserContext context){
        return hook.tagResolver(context, holder.value(), prefix);
    }
}
