package killercreepr.crux.tags.standard.object;

import killercreepr.crux.Crux;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.StringTagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.resolver.Tag;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockTags implements ObjectTag<Block> {
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
}
