package killercreepr.crux.core.loot.item.pool;

import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.loot.functions.LootFunction;
import killercreepr.crux.core.registries.CruxRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LootTableReferenceItemPoolObject extends SimpleItemLootPoolObject{
    protected final @NotNull Key lootTableKey;
    public LootTableReferenceItemPoolObject(int weight, float quality, @Nullable List<LootCondition> conditions,
                                            @Nullable List<LootFunction<ItemStack>> lootFunctions,
                                            @NotNull Key lootTableKey) {
        super(weight, quality, conditions, lootFunctions, (ctx) ->{
            var lootTable = CruxRegistries.ITEM_LOOT_TABLE.get(lootTableKey);
            if(lootTable == null) throw new IllegalStateException("ItemLootTable of " + lootTableKey + " not found!");
            return lootTable.populateLoot(ctx);
        });
        this.lootTableKey = lootTableKey;
    }

    public LootTableReferenceItemPoolObject(int weight, float quality,
                                            @Nullable List<LootFunction<ItemStack>> lootFunctions, Key lootTableKey) {
        this(weight, quality, null, lootFunctions, lootTableKey);
    }

    public LootTableReferenceItemPoolObject(int weight, float quality, Key lootTableKey) {
        this(weight, quality, null, lootTableKey);
    }

    public @NotNull Key getLootTableKey() {
        return lootTableKey;
    }
}
