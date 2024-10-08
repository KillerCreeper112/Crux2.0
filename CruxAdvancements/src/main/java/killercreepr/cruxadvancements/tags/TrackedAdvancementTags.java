package killercreepr.cruxadvancements.tags;

import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.HookedObjectContainer;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.hook.impl.StringHookedObjectTag;
import killercreepr.crux.tags.hook.impl.StringListHookedObjectTag;
import killercreepr.crux.tags.hook.prefix.HookedPrefixBuilder;
import killercreepr.crux.tags.resolver.StringResolver;
import killercreepr.crux.tags.resolver.Tag;
import killercreepr.cruxadvancements.data.TrackedAdvancement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TrackedAdvancementTags implements ObjectTag<TrackedAdvancement> {
    @Override
    public @NotNull Class<TrackedAdvancement> getObjectType() {
        return TrackedAdvancement.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("tracked_advancement_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull TrackedAdvancement object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("advancement_key", (args, ctx) -> object.getAdvancementKey().asString()))
            .add(Tag.string("manager_key", (args, ctx) -> object.getManagerKey() + ""))
            ;

    }

    @Override
    public @Nullable HookedObjectContainer<StringListHookedObjectTag<?>> hookStringLists(@NotNull TrackedAdvancement object, @NotNull TagParser tags) {
        return HookedObjectContainer.stringList()
            .addAll(tags.hookStringLists(object.getAdvancement(), HookedPrefixBuilder.overwrite(
                FormatPrefix.simple("tracked_advancement/advance/")
            )))
            .addAll(tags.hookStringLists(object.getManager(), HookedPrefixBuilder.overwrite(
                FormatPrefix.simple("tracked_advancement/manager/")
            )))
            ;
    }

    @Override
    public @Nullable HookedObjectContainer<StringHookedObjectTag<?>> hookStrings(@NotNull TrackedAdvancement object, @NotNull TagParser tags) {
        return HookedObjectContainer.string()
            .addAll(tags.hookStrings(object.getAdvancement(), HookedPrefixBuilder.overwrite(
                FormatPrefix.simple("tracked_advancement/advance/")
            )))
            .addAll(tags.hookStrings(object.getManager(), HookedPrefixBuilder.overwrite(
                FormatPrefix.simple("tracked_advancement/manager/")
            )))
            ;
    }
}
