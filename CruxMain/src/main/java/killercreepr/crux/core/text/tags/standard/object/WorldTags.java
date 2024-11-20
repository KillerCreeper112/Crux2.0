package killercreepr.crux.core.text.tags.standard.object;

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
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldTags implements ObjectTag<World> {
    @Override
    public @NotNull Class<World> getObjectType() {
        return World.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("world_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull World object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("name", (args, ctx) -> object.getName()))
            .add(Tag.string("uuid", (args, ctx) -> object.getUID().toString()))
            .add(Tag.string("min_height", (args, ctx) -> object.getMinHeight() + ""))
            .add(Tag.string("max_height", (args, ctx) -> object.getMaxHeight() + ""))
            .add(Tag.string("time", (args, ctx) -> object.getTime() + ""))
            .add(Tag.string("full_time", (args, ctx) -> object.getFullTime() + ""))
            .add(Tag.string("game_time", (args, ctx) -> object.getGameTime() + ""))
            .add(Tag.string("is_day_time", (args, ctx) -> object.isDayTime() + ""))
            .add(Tag.string("is_clear_weather", (args, ctx) -> object.isClearWeather() + ""))
            .add(Tag.string("weather_duration", (args, ctx) -> object.getWeatherDuration() + ""))
            .add(Tag.string("clear_weather_duration", (args, ctx) -> object.getClearWeatherDuration() + ""))
            ;
    }

    @Override
    public @Nullable HookedObjectContainer<StringHookedObjectTag<?>> hookStrings(@NotNull World object, @NotNull TagParser tags) {
        return HookedObjectContainer.string().addAll(tags.hookStrings(object.getWorldBorder(),
            HookedPrefixBuilder.overwrite(FormatPrefix.simple("world_border/"))))
            ;
    }

    @Override
    public @Nullable HookedObjectContainer<StringListHookedObjectTag<?>> hookStringLists(@NotNull World object, @NotNull TagParser tags) {
        return HookedObjectContainer.stringList().addAll(tags.hookStringLists(object.getWorldBorder(),
            HookedPrefixBuilder.overwrite(FormatPrefix.simple("world_border/"))))
            ;
    }
}
