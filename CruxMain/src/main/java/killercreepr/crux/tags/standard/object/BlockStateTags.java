package killercreepr.crux.tags.standard.object;

import killercreepr.crux.Crux;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.StringTagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.resolver.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockStateTags implements ObjectTag<BlockState> {
    @Override
    public @NotNull Class<BlockState> getObjectType() {
        return BlockState.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("block_state_");
    }

    @Override
    public @Nullable StringTagContainer requestStrings(@NotNull BlockState item, @NotNull TagParser tags) {
        return new StringTagContainer(tags)
            .add(Tag.string("type", (args, context) -> Crux.handlers().block().getType(item.getBlockData()).asString()))
            ;
    }
}
