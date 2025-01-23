package killercreepr.crux.core.text.tags.standard.object;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.HookedObjectContainer;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.hook.StringHookedObjectTag;
import killercreepr.crux.core.text.hook.StringListHookedObjectTag;
import killercreepr.crux.core.text.resolver.Tag;
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
            .add(Tag.string("block_x", (args, ctx) -> object.getBlockX() + ""))
            .add(Tag.string("block_y", (args, ctx) -> object.getBlockY() + ""))
            .add(Tag.string("block_z", (args, ctx) -> object.getBlockZ() + ""))
            .add(Tag.string("yaw", (args, ctx) -> object.getYaw() + ""))
            .add(Tag.string("pitch", (args, ctx) -> object.getPitch() + ""))
            ;
    }

    @Override
    public @Nullable HookedObjectContainer<StringHookedObjectTag<?>> hookStrings(@NotNull Location object, Object base, Object parent, @NotNull TagParser tags) {
        var hooks =HookedObjectContainer.string();
        World world = object.getWorld();
        if(world != null){
            hooks.addAll(tags.hookStrings(world, base, object, FormatPrefix.simple("world/")));
        }
        return hooks;
    }

    @Override
    public @Nullable HookedObjectContainer<StringListHookedObjectTag<?>> hookStringLists(@NotNull Location object, Object base, Object parent, @NotNull TagParser tags) {
        var hooks =HookedObjectContainer.stringList();
        World world = object.getWorld();
        if(world != null){
            hooks.addAll(tags.hookStringLists(world, base, object, FormatPrefix.simple("world/")));
        }
        return hooks;
    }
}
