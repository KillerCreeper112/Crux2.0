package killercreepr.crux.api.loot.item;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.LootPoolObject;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface ItemLootPoolObject extends LootPoolObject<ItemStack> {
    interface ItemsSupplier{
        @NotNull
        Collection<ItemStack> values(@NotNull LootContext ctx);
    }
}
