package killercreepr.cruxblocks.core.components.persistence;

import killercreepr.crux.api.item.dynamic.component.persistence.DynamicPersistentTag;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.item.dynamic.component.persistence.SimpleDynamicPersistentTag;
import killercreepr.crux.core.persistence.CruxPersistence;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlocksDynamicPersistence {
    public static DynamicPersistentTag<Object, Key> BLOCK_GROUP = new SimpleDynamicPersistentTag<>(CruxPersistence.CRUX_KEY) {
        @Override
        public @Nullable <T extends PersistentDataContainer> Key parse(@NotNull T to, @NotNull TextParserContext ctx, @NotNull Object value) {
            return Crux.key(ctx.deserializeString(value.toString()));
        }
    };
}
