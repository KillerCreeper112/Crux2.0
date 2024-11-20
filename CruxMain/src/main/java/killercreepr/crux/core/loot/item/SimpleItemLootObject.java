package killercreepr.crux.core.loot.item;

import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.loot.functions.LootFunction;
import killercreepr.crux.api.loot.item.ItemLootObject;
import killercreepr.crux.core.loot.SimpleLootObject;
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
