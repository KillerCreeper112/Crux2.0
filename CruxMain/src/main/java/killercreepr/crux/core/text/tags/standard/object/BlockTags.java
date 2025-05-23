package killercreepr.crux.core.text.tags.standard.object;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.SimpleObjectTag;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.text.container.StringTagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BlockTags implements SimpleObjectTag<Block> {
    @Override
    public @NotNull Class<Block> getObjectType() {
        return Block.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("block_");
    }

    @Override
    public @Nullable StringTagContainer requestStrings(@NotNull Block item, @NotNull TagParser tags) {
        return new StringTagContainer(tags)
            .add(Tag.string("type", (args, context) -> Crux.handlers().block().getType(item).asString()))
            .add(Tag.string("x", (args, context) -> item.getX() + ""))
            .add(Tag.string("y", (args, context) -> item.getY() + ""))
            .add(Tag.string("z", (args, context) -> item.getZ() + ""))
            .add(Tag.string("world", (args, context) -> item.getWorld().getName()))
            ;
    }

    @Override
    public @Nullable Map<Object, FormatPrefix> hookObjects(Block object) {
        return Map.of(
            object.getState(), FormatPrefix.simple("state/")
        );
    }
}
