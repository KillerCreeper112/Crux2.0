package killercreepr.cruxadvancements.core.tags;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.HookedObjectContainer;
import killercreepr.crux.api.text.hook.HookedPrefixBuilder;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.hook.StringHookedObjectTag;
import killercreepr.crux.core.text.hook.StringListHookedObjectTag;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.cruxadvancements.core.data.TrackedAdvancement;
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
            .addAll(tags.hookStringLists(object.getAdvancementOrThrow(), HookedPrefixBuilder.overwrite(
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
            .addAll(tags.hookStrings(object.getAdvancementOrThrow(), HookedPrefixBuilder.overwrite(
                FormatPrefix.simple("tracked_advancement/advance/")
            )))
            .addAll(tags.hookStrings(object.getManager(), HookedPrefixBuilder.overwrite(
                FormatPrefix.simple("tracked_advancement/manager/")
            )))
            ;
    }
}
