package killercreepr.crux.tags.tag;

import killerceepr.crux.tags.Tags;
import killerceepr.crux.tags.container.ObjectHookContainer;
import killerceepr.crux.tags.format.FormatPrefix;
import killerceepr.crux.tags.hook.LoreHook;
import killerceepr.crux.tags.hook.StringHook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public abstract class ObjectTag<T> {
    private final @NotNull Class<T> object;

    public Class<T> getObject() {
        return object;
    }

    public ObjectTag(@NotNull Class<T> object) {
        this.object = object;
    }

    public abstract @NotNull FormatPrefix defaultPrefix();
    public boolean canResolve(@NotNull Object object){
        return this.object.isAssignableFrom(object.getClass());
    }
    /**
     * @return Generates other StringHooks that may be tied to this one object.
     */
    public @Nullable Collection<ObjectHookContainer<?>> hookStrings(@NotNull T object, @NotNull Tags tags){
        return null;
    }

    /**
     * @return Generates other LoreHooks that may be tied to this one object.
     */
    public @Nullable Map<Object, Collection<LoreHook<?>>> hookLore(@NotNull T object, @NotNull Tags tags){
        return null;
    }

    /**
     * @return Requests these object's string hooks.
     */
    public @Nullable Collection<StringHook<T>> requestStrings(@NotNull T object, @NotNull Tags tags){
        return null;
    }

    /**
     * @return Requests these object's lore hooks.
     */
    public @Nullable Collection<LoreHook<T>> requestLore(@NotNull T object, @NotNull Tags tags){
        return null;
    }
}
