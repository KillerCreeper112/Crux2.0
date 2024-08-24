package killercreepr.crux.loot.impl.item;

import killercreepr.crux.loot.LootPool;
import killercreepr.crux.loot.impl.SimpleLootTable;
import killercreepr.crux.loot.item.ItemLootTable;
import killercreepr.crux.valueproviders.number.ConstantNumber;
import killercreepr.crux.valueproviders.number.NumberProvider;
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
