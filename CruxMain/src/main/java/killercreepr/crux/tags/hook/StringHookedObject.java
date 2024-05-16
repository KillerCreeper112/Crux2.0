package killercreepr.crux.tags.hook;

import killerceepr.crux.data.Holder;
import killerceepr.crux.tags.Tags;
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

    public @Nullable String request(){
        return request(new String[0]);
    }

    public @Nullable String request(@NotNull String[] args){
        T object = holder.value();
        if(object == null) return null;
        return hook.parseObject(object, args);
    }

    public @Nullable TagResolver tagResolver(){
        T object = holder.value();
        if(object == null) return null;
        return hook.tagResolver(object, hook.identifier());
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
