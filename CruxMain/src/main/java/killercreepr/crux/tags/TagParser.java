package killercreepr.crux.tags;

import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.container.StringListTagContainer;
import killercreepr.crux.tags.container.StringTagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.hook.impl.StringHookedObjectContainer;
import killercreepr.crux.tags.hook.impl.StringListHookedObjectContainer;
import killercreepr.crux.tags.hook.prefix.HookedPrefixBuilder;
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
