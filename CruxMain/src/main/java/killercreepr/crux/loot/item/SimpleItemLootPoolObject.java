package killercreepr.crux.loot.item;

import killercreepr.crux.data.Holder;
import killercreepr.crux.loot.SimpleLootObject;
import killercreepr.crux.loot.api.LootObject;
import killercreepr.crux.loot.api.conditions.LootCondition;
import killercreepr.crux.loot.api.functions.LootFunction;
import killercreepr.crux.loot.item.api.ItemLootPoolObject;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SimpleItemLootPoolObject extends SimpleLootObject<ItemStack> implements ItemLootPoolObject {
    protected final @Nullable Holder<ItemStack> item;
    public SimpleItemLootPoolObject(int weight, float quality, @Nullable Holder<ItemStack> item) {
        super(weight, quality);
        this.item = item;
    }

    public SimpleItemLootPoolObject(int weight, float quality, @Nullable List<LootCondition> conditions, @Nullable List<LootFunction<ItemStack>> lootFunctions, @Nullable Holder<ItemStack> item) {
        super(weight, quality, conditions, lootFunctions);
        this.item = item;
    }

    public SimpleItemLootPoolObject(@NotNull LootObject<ItemStack> object, @Nullable Holder<ItemStack> item) {
        super(object);
        this.item = item;
    }

    @Override
    public @Nullable Holder<ItemStack> getItem() {
        return item;
    }
}
