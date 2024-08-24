package killercreepr.crux.loot.impl.item;

import killercreepr.crux.loot.conditions.LootCondition;
import killercreepr.crux.loot.functions.LootFunction;
import killercreepr.crux.loot.impl.SimpleLootObject;
import killercreepr.crux.loot.item.ItemLootObject;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SimpleItemLootObject extends SimpleLootObject<ItemStack> implements ItemLootObject {
    public SimpleItemLootObject(int weight, float quality, @Nullable List<LootCondition> conditions, @Nullable List<LootFunction<ItemStack>> lootFunctions) {
        super(weight, quality, conditions, lootFunctions);
    }

    public SimpleItemLootObject(int weight, float quality, @Nullable List<LootFunction<ItemStack>> lootFunctions) {
        super(weight, quality, lootFunctions);
    }

    public SimpleItemLootObject(int weight, float quality) {
        super(weight, quality);
    }
}
