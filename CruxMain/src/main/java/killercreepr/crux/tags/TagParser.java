package killercreepr.crux.tags;

import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.container.StringListTagContainer;
import killercreepr.crux.tags.container.StringTagContainer;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.hook.impl.StringHookedObjectContainer;
import killercreepr.crux.tags.hook.impl.StringListHookedObjectContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface TagParser {
    default TagParser register(@NotNull ObjectTag<?>... tags){
        for(ObjectTag<?> t : tags){
            register(t);
        }
        return this;
    }
    default TagParser register(@NotNull Collection<ObjectTag<?>> tags){
        for(ObjectTag<?> t : tags){
            register(t);
        }
        return this;
    }
    CruxTags register(@NotNull ObjectTag<?> tag);
    <T> @NotNull Collection<ObjectTag<T>> locateTags(@NotNull T object);

    <T> @Nullable StringTagContainer buildStringTags(@NotNull T object);
    <T> @Nullable StringTagContainer buildStringTags(@NotNull T object, @Nullable TagsPrefixBuilder prefixBuilder);

    <T> @Nullable StringListTagContainer buildStringListTags(@NotNull T object);
    <T> @Nullable StringListTagContainer buildStringListTags(@NotNull T object, @Nullable TagsPrefixBuilder prefixBuilder);

    <T> @Nullable MergedTagContainer buildTags(@NotNull T object);

    <T> @NotNull StringHookedObjectContainer hookStrings(@NotNull T object);
    <T> @NotNull StringListHookedObjectContainer hookStringLists(@NotNull T object);
}
