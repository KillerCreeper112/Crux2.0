package killercreepr.crux.core.loot.item.pool;

import killercreepr.crux.api.loot.LootTable;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.loot.functions.LootFunction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LootTableItemPoolObject extends SimpleItemLootPoolObject{
    protected final @NotNull LootTable<ItemStack> lootTable;
    public LootTableItemPoolObject(int weight, float quality, @Nullable List<LootCondition> conditions,
                                   @Nullable List<LootFunction<ItemStack>> lootFunctions,
                                   @NotNull LootTable<ItemStack> lootTable) {
        super(weight, quality, conditions, lootFunctions, lootTable::populateLoot);
        this.lootTable = lootTable;
    }

    public LootTableItemPoolObject(int weight, float quality,
                                   @Nullable List<LootFunction<ItemStack>> lootFunctions, LootTable<ItemStack> lootTableKey) {
        this(weight, quality, null, lootFunctions, lootTableKey);
    }

    public LootTableItemPoolObject(int weight, float quality, LootTable<ItemStack> lootTableKey) {
        this(weight, quality, null, lootTableKey);
    }

    public @NotNull LootTable<ItemStack> getLootTable() {
        return lootTable;
    }
}
