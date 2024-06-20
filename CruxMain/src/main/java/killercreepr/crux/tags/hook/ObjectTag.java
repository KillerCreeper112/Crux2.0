package killercreepr.crux.tags.hook;

import killercreepr.crux.tags.container.StringListTagContainer;
import killercreepr.crux.tags.container.StringTagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.TagParser;
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
    default @Nullable StringTagContainer hookStrings(@NotNull T object, @NotNull TagParser tags){
        return null;
    }

    /**
     * @return Generates other LoreHooks that may be tied to this one object.
     */
    default @Nullable StringListTagContainer hookStringLists(@NotNull T object, @NotNull TagParser tags){
        return null;
    }

    /**
     * @return Requests these object's string hooks.
     */
    default @Nullable StringTagContainer requestStrings(@NotNull T object, @NotNull TagParser tags){
        return null;
    }

    /**
     * @return Requests these object's lore hooks.
     */
    default @Nullable StringListTagContainer requestStringLists(@NotNull T object, @NotNull TagParser tags){
        return null;
    }
}
