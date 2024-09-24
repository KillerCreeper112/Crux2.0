package killercreepr.crux.tags.hook;

import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.impl.StringHookedObjectTag;
import killercreepr.crux.tags.hook.impl.StringListHookedObjectTag;
import killercreepr.crux.tags.resolver.StringListResolver;
import killercreepr.crux.tags.resolver.StringResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ObjectTag<T> {
    @NotNull Class<T> getObjectType();

    @NotNull
    FormatPrefix defaultPrefix();
    default boolean canResolve(@NotNull Object object){
        return this.getObjectType().isAssignableFrom(object.getClass());
    }
    /**
     * @return Generates other StringHooks that may be tied to this one object.
     */
    default @Nullable HookedObjectContainer<StringHookedObjectTag<?>> hookStrings(@NotNull T object, @NotNull TagParser tags){
        return null;
    }

    /**
     * @return Generates other LoreHooks that may be tied to this one object.
     */
    default @Nullable HookedObjectContainer<StringListHookedObjectTag<?>> hookStringLists(@NotNull T object, @NotNull TagParser tags){
        return null;
    }

    /**
     * @return Requests these object's string hooks.
     */
    default @Nullable TagContainer<StringResolver> requestStrings(@NotNull T object, @NotNull TagParser tags){
        return null;
    }

    /**
     * @return Requests these object's lore hooks.
     */
    default @Nullable TagContainer<StringListResolver> requestStringLists(@NotNull T object, @NotNull TagParser tags){
        return null;
    }
}
