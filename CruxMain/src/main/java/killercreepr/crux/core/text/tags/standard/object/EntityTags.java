package killercreepr.crux.core.text.tags.standard.object;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.HookedObjectContainer;
import killercreepr.crux.api.text.hook.HookedPrefixBuilder;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.text.container.StringTagContainer;
import killercreepr.crux.core.text.hook.StringHookedObjectTag;
import killercreepr.crux.core.text.hook.StringListHookedObjectTag;
import killercreepr.crux.core.text.resolver.Tag;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityTags implements ObjectTag<Entity> {
    @Override
    public @NotNull Class<Entity> getObjectType() {
        return Entity.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("entity_");
    }

    @Override
    public @Nullable StringTagContainer requestStrings(@NotNull Entity p, @NotNull TagParser tags) {
        return new StringTagContainer(tags)
            .add(Tag.string("name", (args, context) -> p.getName() + ""))
            .add(Tag.string("x", (args, ctx) -> p.getX() + ""))
            .add(Tag.string("y", (args, ctx) -> p.getY() + ""))
            .add(Tag.string("z", (args, ctx) -> p.getZ() + ""))
            ;
    }

    @Override
    public @Nullable HookedObjectContainer<StringHookedObjectTag<?>> hookStrings(@NotNull Entity object, @NotNull TagParser tags) {
        return HookedObjectContainer.string()
            .addAll(tags.hookStrings(object.getWorld(), HookedPrefixBuilder.overwrite(
                FormatPrefix.simple("entity_world/")
            )))
            ;
    }

    @Override
    public @Nullable HookedObjectContainer<StringListHookedObjectTag<?>> hookStringLists(@NotNull Entity object, @NotNull TagParser tags) {
        return HookedObjectContainer.stringList()
            .addAll(tags.hookStringLists(object.getWorld(), HookedPrefixBuilder.overwrite(
                FormatPrefix.simple("entity_world/")
            )))
            ;
    }
}
