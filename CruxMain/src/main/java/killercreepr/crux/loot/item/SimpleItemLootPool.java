package killercreepr.crux.loot.item;

import killercreepr.crux.loot.SimpleLootPool;
import killercreepr.crux.loot.api.LootObject;
import killercreepr.crux.loot.api.LootPoolObject;
import killercreepr.crux.loot.item.api.ItemLootPool;
import killercreepr.crux.valueproviders.number.NumberProvider;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimpleItemLootPool extends SimpleLootPool<ItemStack> implements ItemLootPool {
    public SimpleItemLootPool(@NotNull NumberProvider rolls, int weight, float quality, @NotNull List<LootPoolObject<ItemStack>> data) {
        super(rolls, weight, quality, data);
    }

    public SimpleItemLootPool(@NotNull NumberProvider rolls, @NotNull LootObject<ItemStack> object, @NotNull List<LootPoolObject<ItemStack>> data) {
        super(rolls, object, data);
    }
}
