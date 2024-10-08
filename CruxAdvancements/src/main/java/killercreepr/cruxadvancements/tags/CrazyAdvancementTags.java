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
import killercreepr.cruxadvancements.advancement.progression.CruxAdvancementProgress;
import killercreepr.cruxadvancements.crazy.CrazyAdvancement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CrazyAdvancementTags implements ObjectTag<CrazyAdvancement> {
    @Override
    public @NotNull Class<CrazyAdvancement> getObjectType() {
        return CrazyAdvancement.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("advancement_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull CrazyAdvancement object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("key", (args, ctx) -> object.key().asString()))
            .add(Tag.string("parent", (args, ctx) -> object.parent() + ""))
            .add(Tag.string("is_granted", ((args, ctx) -> {
                UUID uuid = UUID.fromString(ctx.deserializeString(args.get(0)));
                CruxAdvancementProgress progress = object.getProgressIfPresent(uuid);
                return (progress != null && progress.isDone()) + "";
            })))
            ;

    }

    @Override
    public @Nullable HookedObjectContainer<StringListHookedObjectTag<?>> hookStringLists(@NotNull CrazyAdvancement object, @NotNull TagParser tags) {
        return HookedObjectContainer.stringList()
            .addAll(tags.hookStringLists(object.getDisplay(), HookedPrefixBuilder.overwrite(
                FormatPrefix.simple("advancement_display/")
            )))
            .addAll(tags.hookStringLists(object.getIcon(), HookedPrefixBuilder.overwrite(
                FormatPrefix.simple("advancement_icon/")
            )))
            ;
    }

    @Override
    public @Nullable HookedObjectContainer<StringHookedObjectTag<?>> hookStrings(@NotNull CrazyAdvancement object, @NotNull TagParser tags) {
        return HookedObjectContainer.string()
            .addAll(tags.hookStrings(object.getDisplay(), HookedPrefixBuilder.overwrite(
                FormatPrefix.simple("advancement_display/")
            )))
            .addAll(tags.hookStrings(object.getIcon(), HookedPrefixBuilder.overwrite(
                FormatPrefix.simple("advancement_icon/")
            )))
            ;
    }
}
