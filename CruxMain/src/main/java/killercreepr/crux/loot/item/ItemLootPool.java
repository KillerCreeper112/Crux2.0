package killercreepr.crux.loot.item;

import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.LootPool;
import killercreepr.crux.util.CruxItem;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public interface ItemLootPool extends LootPool<ItemStack> {
    default void fillInventory(@NotNull Inventory inventory, @NotNull LootContext context) {
        Random random = context.getRandom();
        for(ItemStack i : populateLoot(context)){
            int index = random.nextInt(0, inventory.getSize());
            ItemStack empty = inventory.getItem(index);
            while(!CruxItem.isEmpty(empty)){
                index++;
                if(index >= inventory.getSize()) return;
                empty = inventory.getItem(index);
            }
            inventory.setItem(index, i);
        }
    }
}
