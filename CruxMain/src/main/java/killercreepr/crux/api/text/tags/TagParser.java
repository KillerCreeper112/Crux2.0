package killercreepr.crux.api.text.tags;

import killercreepr.crux.api.text.hook.HookedPrefixBuilder;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.core.text.container.StringListTagContainer;
import killercreepr.crux.core.text.container.StringTagContainer;
import killercreepr.crux.core.text.hook.StringHookedObjectContainer;
import killercreepr.crux.core.text.hook.StringListHookedObjectContainer;
import killercreepr.crux.core.text.tags.CruxTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface TagParser {
    static Builder builder(){
        return new CruxTags.Builder();
    }

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

    default <T> @NotNull StringHookedObjectContainer hookStrings(@NotNull T object){
        return hookStrings(object, HookedPrefixBuilder.empty());
    }

    <T> @NotNull StringHookedObjectContainer hookStrings(@NotNull T object, @NotNull HookedPrefixBuilder prefix);

    default <T> @NotNull StringListHookedObjectContainer hookStringLists(@NotNull T object){
        return hookStringLists(object, HookedPrefixBuilder.empty());
    }

    <T> @NotNull StringListHookedObjectContainer hookStringLists(@NotNull T object, @NotNull HookedPrefixBuilder prefix);

    interface Builder{
        Builder addTag(@NotNull ObjectTag<?> tag);
        Builder addTags(@NotNull Collection<ObjectTag<?>> tags);
        @NotNull TagParser build();
    }
}
