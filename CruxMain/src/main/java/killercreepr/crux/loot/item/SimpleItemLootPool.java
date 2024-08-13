package killercreepr.crux.loot.item;

import killercreepr.crux.loot.SimpleLootPool;
import killercreepr.crux.loot.api.LootObject;
import killercreepr.crux.loot.api.LootPoolObject;
import killercreepr.crux.loot.api.conditions.LootCondition;
import killercreepr.crux.loot.api.functions.LootFunction;
import killercreepr.crux.loot.item.api.ItemLootPool;
import killercreepr.crux.valueproviders.number.NumberProvider;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SimpleItemLootPool extends SimpleLootPool<ItemStack> implements ItemLootPool {
    public SimpleItemLootPool(int weight, float quality, @NotNull NumberProvider rolls, @NotNull List<LootPoolObject<ItemStack>> data) {
        super(weight, quality, rolls, data);
    }

    public SimpleItemLootPool(int weight, float quality, @Nullable List<LootCondition> conditions, @Nullable List<LootFunction<ItemStack>> lootFunctions, @NotNull NumberProvider rolls, @NotNull List<LootPoolObject<ItemStack>> data) {
        super(weight, quality, conditions, lootFunctions, rolls, data);
    }

    public SimpleItemLootPool(@NotNull LootObject<ItemStack> object, @NotNull NumberProvider rolls, @NotNull List<LootPoolObject<ItemStack>> data) {
        super(object, rolls, data);
    }
}
