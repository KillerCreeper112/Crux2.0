package killercreepr.cruxconfig.config.bukkit.handler.impl.item.component.persistence;

import killercreepr.crux.item.dynamic.components.persistence.DynamicPersistentTag;
import killercreepr.crux.item.dynamic.components.persistence.TypedDynamicPersistentTag;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FileDynamicPersistTagParser<T> extends Keyed {
    @NotNull
    FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull TypedDynamicPersistentTag object);
    @Nullable
    TypedDynamicPersistentTag deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e);

    @Nullable T parseObject(@NotNull FileContext<?> ctx, @NotNull FileObject base, @NotNull FileElement e);

    @NotNull
    DynamicPersistentTag<T, ?> getDynamicTag(@NotNull FileContext<?> ctx, @NotNull FileElement e);
}
