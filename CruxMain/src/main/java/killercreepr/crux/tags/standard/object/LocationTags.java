package killercreepr.crux.tags.standard.object;

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
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LocationTags implements ObjectTag<Location> {
    @Override
    public @NotNull Class<Location> getObjectType() {
        return Location.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("location_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull Location object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("x", (args, ctx) -> object.x() + ""))
            .add(Tag.string("y", (args, ctx) -> object.y() + ""))
            .add(Tag.string("z", (args, ctx) -> object.z() + ""))
            .add(Tag.string("yaw", (args, ctx) -> object.getYaw() + ""))
            .add(Tag.string("pitch", (args, ctx) -> object.getPitch() + ""))
            ;
    }


    @Override
    public @Nullable HookedObjectContainer<StringListHookedObjectTag<?>> hookStringLists(@NotNull Location object, @NotNull TagParser tags) {
        var hooks = HookedObjectContainer.stringList();
        World world = object.getWorld();
        if(world != null) hooks.addAll(tags.hookStringLists(world, HookedPrefixBuilder.overwrite(
            FormatPrefix.simple("location_world/")
        )));
        return hooks;
    }
    @Override
    public @Nullable HookedObjectContainer<StringHookedObjectTag<?>> hookStrings(@NotNull Location object, @NotNull TagParser tags) {
        var hooks = HookedObjectContainer.string();
        World world = object.getWorld();
        if(world != null) hooks.addAll(tags.hookStrings(world, HookedPrefixBuilder.overwrite(
            FormatPrefix.simple("location_world/")
        )));
        return hooks;
    }
}
