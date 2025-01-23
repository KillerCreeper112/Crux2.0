package killercreepr.crux.core.text.tags.standard.object;

import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxLocationTags implements ObjectTag<CruxLocation> {
    @Override
    public @NotNull Class<CruxLocation> getObjectType() {
        return CruxLocation.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("crux_location_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull CruxLocation object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("x", (args, ctx) -> object.x() + ""))
            .add(Tag.string("y", (args, ctx) -> object.y() + ""))
            .add(Tag.string("z", (args, ctx) -> object.z() + ""))
            .add(Tag.string("block_x", (args, ctx) -> object.blockX() + ""))
            .add(Tag.string("block_y", (args, ctx) -> object.blockY() + ""))
            .add(Tag.string("block_z", (args, ctx) -> object.blockZ() + ""))
            .add(Tag.string("yaw", (args, ctx) -> object.yaw() + ""))
            .add(Tag.string("pitch", (args, ctx) -> object.pitch() + ""))
            ;
    }
}
