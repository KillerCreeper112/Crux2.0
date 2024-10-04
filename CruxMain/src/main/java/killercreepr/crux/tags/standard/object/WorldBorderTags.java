package killercreepr.crux.tags.standard.object;

import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.resolver.StringResolver;
import killercreepr.crux.tags.resolver.Tag;
import org.bukkit.WorldBorder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldBorderTags implements ObjectTag<WorldBorder> {
    @Override
    public @NotNull Class<WorldBorder> getObjectType() {
        return WorldBorder.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("world_border_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull WorldBorder object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("size", (args, ctx) -> object.getSize() + ""))
            .add(Tag.string("max_size", (args, ctx) -> object.getMaxSize() + ""))
            .add(Tag.string("damage_amount", (args, ctx) -> object.getDamageAmount() + ""))
            .add(Tag.string("damage_buffer", (args, ctx) -> object.getDamageBuffer() + ""))
            .add(Tag.string("warning_time", (args, ctx) -> object.getWarningTime() + ""))
            .add(Tag.string("warning_distance", (args, ctx) -> object.getWarningDistance() + ""))
            .add(Tag.string("max_center_coordinate", (args, ctx) -> object.getMaxCenterCoordinate() + ""))
            ;
    }
}
