package killercreepr.cruxblocks.config;

import killercreepr.crux.Crux;
import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.item.dynamic.components.DynamicPersistTagHandler;
import killercreepr.crux.item.dynamic.components.DynamicPersistentTag;
import killercreepr.cruxblocks.persistence.CruxBlocksPersistTags;
import killercreepr.cruxblocks.registeries.CruxBlocksRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxConfigHook {
    public static void register(){
        DynamicPersistentTag.HANDLERS.register("block_group", new DynamicPersistTagHandler() {
            @Override
            public <T extends PersistentDataContainer> void handle(@NotNull DynamicPersistentTag tag, @NotNull T to,
                                                                   @Nullable Key key,
                                                                   @NotNull Object value, @NotNull TextParserContext ctx) {
                Key groupKey = Crux.key(ctx.deserializeString(value.toString()));
                CruxBlocksPersistTags.CRUX_BLOCK_GROUP.set(to, CruxBlocksRegistries.BLOCKS.getGroup(groupKey));
            }
        });
    }
}
