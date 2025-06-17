package killercreepr.crux.core.loot.item;

import killercreepr.crux.api.loot.LootPoolObject;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.loot.functions.LootFunction;
import killercreepr.crux.api.loot.item.ItemLootPool;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.loot.SimpleLootPool;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SimpleItemLootPool extends SimpleLootPool<ItemStack> implements ItemLootPool {
    public SimpleItemLootPool(@Nullable List<LootCondition> conditions, @Nullable List<LootFunction<ItemStack>> lootFunctions,
                              @NotNull NumberProvider rolls, @NotNull List<LootPoolObject<ItemStack>> data,
                              boolean allowDuplicates) {
        super(conditions, lootFunctions, rolls, data, allowDuplicates);
    }

    public SimpleItemLootPool(@Nullable List<LootFunction<ItemStack>> lootFunctions, @NotNull NumberProvider rolls, @NotNull List<LootPoolObject<ItemStack>> data,
                              boolean allowDuplicates) {
        super(lootFunctions, rolls, data, allowDuplicates);
    }

    public SimpleItemLootPool(@NotNull NumberProvider rolls, @NotNull List<LootPoolObject<ItemStack>> data,
                              boolean allowDuplicates) {
        super(rolls, data, allowDuplicates);
    }
}
