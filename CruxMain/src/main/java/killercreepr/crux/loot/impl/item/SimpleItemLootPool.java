package killercreepr.crux.loot.impl.item;

import killercreepr.crux.loot.LootPoolObject;
import killercreepr.crux.loot.conditions.LootCondition;
import killercreepr.crux.loot.functions.LootFunction;
import killercreepr.crux.loot.impl.SimpleLootPool;
import killercreepr.crux.loot.item.ItemLootPool;
import killercreepr.crux.valueproviders.number.NumberProvider;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SimpleItemLootPool extends SimpleLootPool<ItemStack> implements ItemLootPool {
    public SimpleItemLootPool(@Nullable List<LootCondition> conditions, @Nullable List<LootFunction<ItemStack>> lootFunctions, @NotNull NumberProvider rolls, @NotNull List<LootPoolObject<ItemStack>> data) {
        super(conditions, lootFunctions, rolls, data);
    }

    public SimpleItemLootPool(@Nullable List<LootFunction<ItemStack>> lootFunctions, @NotNull NumberProvider rolls, @NotNull List<LootPoolObject<ItemStack>> data) {
        super(lootFunctions, rolls, data);
    }

    public SimpleItemLootPool(@NotNull NumberProvider rolls, @NotNull List<LootPoolObject<ItemStack>> data) {
        super(rolls, data);
    }
}
