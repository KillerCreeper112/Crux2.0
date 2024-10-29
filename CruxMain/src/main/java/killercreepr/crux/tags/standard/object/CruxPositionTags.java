package killercreepr.crux.tags.standard.object;

import killercreepr.crux.data.world.CruxPosition;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.resolver.StringResolver;
import killercreepr.crux.tags.resolver.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxPositionTags implements ObjectTag<CruxPosition> {
    @Override
    public @NotNull Class<CruxPosition> getObjectType() {
        return CruxPosition.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("crux_pos_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull CruxPosition object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("x", (args, ctx) -> object.x() + ""))
            .add(Tag.string("y", (args, ctx) -> object.y() + ""))
            .add(Tag.string("z", (args, ctx) -> object.z() + ""))
            ;
    }
}
