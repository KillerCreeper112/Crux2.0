package killercreepr.crux.core.text.tags.standard.object;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.resolver.Tag;
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
