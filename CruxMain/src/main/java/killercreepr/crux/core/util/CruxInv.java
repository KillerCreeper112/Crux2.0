package killercreepr.crux.core.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CruxInv {
    public static int firstPartial(Inventory inv, ItemStack item) {
        ItemStack[] inventory = inv.getStorageContents();
        if (item != null) {
            for (int i = 0; i < inventory.length; ++i) {
                ItemStack cItem = inventory[i];
                if (cItem != null && cItem.getAmount() < cItem.getMaxStackSize() && cItem.isSimilar(item)) {
                    return i;
                }
            }

        }
        return -1;
    }

    public static boolean hasSpace(Inventory inv, ItemStack... items) {
        return calculateLeftOver(inv, items).isEmpty();
    }

    public static Map<Integer, ItemStack> calculateLeftOver(Inventory inv, ItemStack... items) {
        HashMap<Integer, ItemStack> leftover = new HashMap<>();

        for(int i = 0; i < items.length; ++i) {
            ItemStack item = items[i];
            item = item.clone();

            while(true) {
                int firstPartial = firstPartial(inv, item);
                if (firstPartial == -1) {
                    int firstFree = inv.firstEmpty();
                    if (firstFree == -1) {
                        leftover.put(i, item);
                        break;
                    }

                    int maxStackSize = item.getMaxStackSize();
                    if (item.getAmount() <= maxStackSize) {
                        break;
                    }

                    item.setAmount(item.getAmount() - maxStackSize);
                } else {
                    ItemStack partialItem = inv.getItem(firstPartial).clone();
                    int amount = item.getAmount();
                    int partialAmount = partialItem.getAmount();
                    int maxAmount = partialItem.getMaxStackSize();
                    if (amount + partialAmount <= maxAmount) {
                        partialItem.setAmount(amount + partialAmount);
                        break;
                    }

                    partialItem.setAmount(maxAmount);
                    item.setAmount(amount + partialAmount - maxAmount);
                }
            }
        }

        return leftover;
    }
}
