package killercreepr.crux.core.util;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.item.CruxItem;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class CruxInv {
    public static int removeItems(Inventory inv, Predicate<ItemStack> filter, int amount){
        int removed = 0;
        for (ItemStack item : inv) {
            if(CruxItem.isEmpty(item)) continue;
            if(!filter.test(item)) continue;
            int canRemove = Math.min(amount - removed, item.getAmount());
            if(canRemove < 1) continue;

            item.setAmount(item.getAmount() - canRemove);
            removed += canRemove;
            if(removed >= amount) break;
        }
        return removed;
    }

    public static boolean hasItems(Inventory inv, Predicate<ItemStack> filter, int amount){
        return getItemCount(inv, filter, amount) >= amount;
    }

    public static int getItemCount(Inventory inv, Predicate<ItemStack> filter, int maxAmount){
        int has = 0;
        for (ItemStack item : inv) {
            if(CruxItem.isEmpty(item)) continue;
            if(!filter.test(item)) continue;

            has += item.getAmount();
            if(maxAmount > 0 && has >= maxAmount) break;
        }
        return has;
    }

    public static boolean hasItems(Iterable<Holder<Iterable<ItemStack>>> inv, Predicate<ItemStack> filter, int amount){
        return getItemCount(inv, filter, amount) >= amount;
    }

    public static int getItemCount(Iterable<Holder<Iterable<ItemStack>>> inv, Predicate<ItemStack> filter, int maxAmount){
        int has = 0;
        for (Holder<Iterable<ItemStack>> holder : inv) {
            var items = holder.value();
            if(items == null) continue;
            for (ItemStack item : items) {
                if(CruxItem.isEmpty(item)) continue;
                if(!filter.test(item)) continue;

                has += item.getAmount();
                if(maxAmount > 0 && has >= maxAmount) break;
            }
        }
        return has;
    }

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
