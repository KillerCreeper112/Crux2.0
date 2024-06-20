package killercreepr.crux.tags.ab.hook;

import killercreepr.crux.tags.Tags;
import killercreepr.crux.tags.container.ObjectHookContainer;
import killercreepr.crux.tags.format.FormatPrefix;
import killercreepr.crux.tags.hook.lore.LoreHook;
import killercreepr.crux.tags.hook.string.StringHook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public interface ObjectTag<T> {
    @NotNull Class<T> getObjectType();

    @NotNull FormatPrefix defaultPrefix();
    default boolean canResolve(@NotNull Object object){
        return this.getObjectType().isAssignableFrom(object.getClass());
    }
    /**
     * @return Generates other StringHooks that may be tied to this one object.
     */
    default @Nullable Collection<ObjectHookContainer<?>> hookStrings(@NotNull T object, @NotNull Tags tags){
        return null;
    }

    /**
     * @return Generates other LoreHooks that may be tied to this one object.
     */
    default @Nullable Map<Object, Collection<LoreHook<?>>> hookLore(@NotNull T object, @NotNull Tags tags){
        return null;
    }

    /**
     * @return Requests these object's string hooks.
     */
    default @Nullable Collection<StringHook<T>> requestStrings(@NotNull T object, @NotNull Tags tags){
        return null;
    }

    /**
     * @return Requests these object's lore hooks.
     */
    default @Nullable Collection<LoreHook<T>> requestLore(@NotNull T object, @NotNull Tags tags){
        return null;
    }
}
