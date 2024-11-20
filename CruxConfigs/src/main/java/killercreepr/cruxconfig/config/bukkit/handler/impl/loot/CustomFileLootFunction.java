package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import killercreepr.crux.api.loot.item.ItemLootFunction;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomFileLootFunction<T extends ItemLootFunction> {
    @NotNull String getType();
    @Nullable
    T deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target);
}
