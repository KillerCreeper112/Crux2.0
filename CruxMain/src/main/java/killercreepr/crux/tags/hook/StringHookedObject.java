package killercreepr.crux.tags.hook;

import killercreepr.crux.data.Holder;
import killercreepr.crux.tags.FormatArgs;
import killercreepr.crux.tags.FormatContext;
import killercreepr.crux.tags.Tags;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringHookedObject<T> implements HookedObject<T, StringHook<T>> {
    private final @NotNull Tags tags;
    private final @NotNull String prefix;
    private final @NotNull Holder<T> holder;
    private final @NotNull StringHook<T> hook;

    public StringHookedObject(@NotNull Tags tags, @NotNull String prefix, @NotNull Holder<T> holder, @NotNull StringHook<T> hook) {
        this.tags = tags;
        this.prefix = prefix;
        this.holder = holder;
        this.hook = hook;
    }

    public @Nullable String request(@NotNull FormatContext context){
        return request(new FormatArgs(new String[0]), context);
    }

    public @Nullable String request(@NotNull FormatArgs args, @NotNull FormatContext context){
        T object = holder.value();
        if(object == null) return null;
        return hook.parseObject(object, args, context);
    }

    public @Nullable TagResolver tagResolver(@NotNull FormatContext context){
        T object = holder.value();
        if(object == null) return null;
        return hook.tagResolver(context, object, hook.identifier());
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
