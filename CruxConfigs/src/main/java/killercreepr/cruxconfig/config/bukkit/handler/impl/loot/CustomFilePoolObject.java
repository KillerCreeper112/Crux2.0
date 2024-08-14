package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import killercreepr.crux.loot.api.LootPoolObject;
import killercreepr.crux.loot.api.conditions.LootCondition;
import killercreepr.crux.loot.item.api.ItemLootPoolObject;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomFilePoolObject<T extends ItemLootPoolObject> {
    @NotNull String getType();
    @Nullable
    T deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e);
}
