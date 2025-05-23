package killercreepr.crux.core.text.tags.standard.object;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.SimpleObjectTag;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.text.container.StringTagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockDataTags implements SimpleObjectTag<BlockData> {
    @Override
    public @NotNull Class<BlockData> getObjectType() {
        return BlockData.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("block_data_");
    }

    @Override
    public @Nullable StringTagContainer requestStrings(@NotNull BlockData item, @NotNull TagParser tags) {
        return new StringTagContainer(tags)
            .add(Tag.string("type", (args, context) -> Crux.handlers().block().getType(item).asString()))
            .add(Tag.string("ageable/age", (args, context) ->{
                if(item instanceof Ageable ageable) return ageable.getAge() + "";
                return "0";
            }))
            .add(Tag.string("ageable/max_age", (args, context) ->{
                if(item instanceof Ageable ageable) return ageable.getMaximumAge() + "";
                return "0";
            }))
            ;
    }
}
