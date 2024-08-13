package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import killercreepr.crux.loot.api.functions.LootFunction;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomFileLootFunction<T extends LootFunction<?>> {
    @NotNull String getType();
    @Nullable
    T deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target);
}
