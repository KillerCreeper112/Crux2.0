package killercreepr.crux.tags.hook.string;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.data.Holder;
import killercreepr.crux.tags.FormatArgs;
import killercreepr.crux.tags.Tags;
import killercreepr.crux.tags.hook.HookedObject;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringHookedObject<T> implements HookedObject<T, StringHook<T>> {
    protected final @NotNull Tags tags;
    protected final @NotNull String prefix;
    protected final @NotNull Holder<T> holder;
    protected final @NotNull StringHook<T> hook;

    public StringHookedObject(@NotNull Tags tags, @NotNull String prefix, @NotNull Holder<T> holder, @NotNull StringHook<T> hook) {
        this.tags = tags;
        this.prefix = prefix;
        this.holder = holder;
        this.hook = hook;
    }

    public @Nullable String request(@NotNull TextParserContext context){
        return request(new FormatArgs(new String[0]), context);
    }

    public @Nullable String request(@NotNull FormatArgs args, @NotNull TextParserContext context){
        T object = holder.value();
        if(object == null) return null;
        return hook.parseObject(object, args, context);
    }

    public @Nullable TagResolver tagResolver(@NotNull TextParserContext context){
        T object = holder.value();
        if(object == null) return null;
        return hook.tagResolver(context, object, prefix);
    }

    @Override
    public @NotNull Tags tags() {
        return tags;
    }

    @Override
    public @NotNull String identifier(){
        return prefix + hook.identifier();
    }
    @Override
    public @NotNull String getPrefix() {
        return prefix;
    }
    @Override
    public @NotNull Holder<T> getHolder() {
        return holder;
    }
    @Override
    public @NotNull StringHook<T> getHook() {
        return hook;
    }
}
