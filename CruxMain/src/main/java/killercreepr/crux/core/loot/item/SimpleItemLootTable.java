package killercreepr.crux.core.loot.item;

import killercreepr.crux.api.loot.LootPool;
import killercreepr.crux.api.loot.item.ItemLootTable;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.loot.SimpleLootTable;
import killercreepr.crux.core.valueproviders.number.ConstantNumber;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimpleItemLootTable extends SimpleLootTable<ItemStack> implements ItemLootTable {
    public static SimpleItemLootTable empty(@NotNull Key key){
        return new SimpleItemLootTable(
            key, new ConstantNumber(0), List.of()
        );
    }
    public SimpleItemLootTable(@NotNull Key key, @NotNull NumberProvider rolls, @NotNull List<LootPool<ItemStack>> lootPools) {
        super(key, rolls, lootPools);
    }
}
