package killercreepr.crux.core.text.tags.standard.object;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VectorTags implements ObjectTag<Vector> {
    @Override
    public @NotNull Class<Vector> getObjectType() {
        return Vector.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("vector_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull Vector object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("x", (args, ctx) -> object.getX() + ""))
            .add(Tag.string("y", (args, ctx) -> object.getY() + ""))
            .add(Tag.string("z", (args, ctx) -> object.getZ() + ""))
            .add(Tag.string("block_x", (args, ctx) -> object.getBlockX() + ""))
            .add(Tag.string("block_y", (args, ctx) -> object.getBlockY() + ""))
            .add(Tag.string("block_z", (args, ctx) -> object.getBlockZ() + ""))
            ;
    }
}
