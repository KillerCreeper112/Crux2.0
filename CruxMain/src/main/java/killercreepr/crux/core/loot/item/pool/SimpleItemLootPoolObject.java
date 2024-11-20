package killercreepr.crux.core.loot.item.pool;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.loot.functions.LootFunction;
import killercreepr.crux.api.loot.item.ItemLootPoolObject;
import killercreepr.crux.core.loot.SimpleLootObject;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class SimpleItemLootPoolObject extends SimpleLootObject<ItemStack> implements ItemLootPoolObject {
    protected final @Nullable Holder<Collection<ItemStack>> item;

    public SimpleItemLootPoolObject(int weight, float quality, @Nullable List<LootCondition> conditions, @Nullable List<LootFunction<ItemStack>> lootFunctions, @Nullable Holder<Collection<ItemStack>> item) {
        super(weight, quality, conditions, lootFunctions);
        this.item = item;
    }

    public SimpleItemLootPoolObject(int weight, float quality, @Nullable List<LootFunction<ItemStack>> lootFunctions, @Nullable Holder<Collection<ItemStack>> item) {
        super(weight, quality, lootFunctions);
        this.item = item;
    }

    public SimpleItemLootPoolObject(int weight, float quality, @Nullable Holder<Collection<ItemStack>> item) {
        super(weight, quality);
        this.item = item;
    }

    @Override
    public @Nullable Holder<Collection<ItemStack>> getItems(@NotNull LootContext ctx) {
        return item;
    }
}
