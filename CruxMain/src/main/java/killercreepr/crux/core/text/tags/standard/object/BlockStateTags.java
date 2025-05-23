package killercreepr.crux.core.text.tags.standard.object;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.SimpleObjectTag;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.text.container.StringTagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BlockStateTags implements SimpleObjectTag<BlockState> {
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
            .add(Tag.string("type", (args, context) -> Crux.handlers().block().getType(item).asString()))
            ;
    }

    @Override
    public @Nullable Map<Object, FormatPrefix> hookObjects(BlockState object) {
        return Map.of(
            object.getBlockData(), FormatPrefix.simple("data/")
        );
    }
}
