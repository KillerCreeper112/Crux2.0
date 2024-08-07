package killercreepr.crux.item.dynamic.components;

import killercreepr.crux.context.TextParserContext;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DynamicPersistTagHandler {
    <T extends PersistentDataContainer> void handle(@NotNull DynamicPersistentTag tag,
                                                    @NotNull T to,
                                                    @Nullable Key key, @NotNull Object value,
                                                    @NotNull TextParserContext ctx);
}
