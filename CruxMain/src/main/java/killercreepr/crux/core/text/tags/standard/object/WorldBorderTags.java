package killercreepr.crux.core.text.tags.standard.object;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.resolver.Tag;
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
