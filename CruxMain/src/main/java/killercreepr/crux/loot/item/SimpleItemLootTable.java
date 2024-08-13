package killercreepr.crux.loot.item;

import killercreepr.crux.loot.SimpleLootTable;
import killercreepr.crux.loot.api.LootPool;
import killercreepr.crux.loot.item.api.ItemLootTable;
import killercreepr.crux.valueproviders.number.NumberProvider;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimpleItemLootTable extends SimpleLootTable<ItemStack> implements ItemLootTable {
    public SimpleItemLootTable(@NotNull Key key, @NotNull NumberProvider rolls, @NotNull List<LootPool<ItemStack>> lootPools) {
        super(key, rolls, lootPools);
    }
}
