package killercreepr.crux.tags.ab.hook.string;

import killercreepr.crux.data.Holder;
import killercreepr.crux.tags.ab.hook.HookedObjectResolver;
import killercreepr.crux.tags.ab.resolver.StringResolver;
import killercreepr.crux.tags.hook.string.StringHook;
import org.jetbrains.annotations.NotNull;

public abstract class HookedStringTag<T> implements StringResolver, HookedObjectResolver<T, String> {
    protected final @NotNull String prefix;
    protected final @NotNull Holder<T> holder;
    protected final @NotNull StringHook<T> hook;
    protected HookedStringTag(@NotNull String prefix, @NotNull Holder<T> holder, @NotNull StringHook<T> hook) {
        this.prefix = prefix;
        this.holder = holder;
        this.hook = hook;
    }

    @Override
    public @NotNull Holder<T> getHookedObject() {
        return holder;
    }

    @Override
    public @NotNull String getPrefix() {
        return prefix;
    }

    /*@Override
    public boolean has(@NotNull String name) {
        return name.equalsIgnoreCase(prefix + hook.identifier());
    }*/
}
