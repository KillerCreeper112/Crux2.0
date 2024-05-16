package killercreepr.crux.tags.container;

import killerceepr.crux.data.Holder;
import killerceepr.crux.tags.hook.StringHook;
import killerceepr.crux.tags.tag.ObjectTag;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class ObjectHookContainer<T> {
    private final Holder<T> holder;
    private final ObjectTag<T> objectTag;
    private final Collection<StringHook<T>> hooks;

    public ObjectHookContainer(Holder<T> holder, ObjectTag<T> objectTag, Collection<StringHook<T>> hooks) {
        this.holder = holder;
        this.objectTag = objectTag;
        this.hooks = hooks;
    }

    public @NotNull Holder<T> getHolder() {
        return holder;
    }

    public ObjectTag<T> getObjectTag() {
        return objectTag;
    }

    public Collection<StringHook<T>> getHooks() {
        return hooks;
    }
}
