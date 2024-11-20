package killercreepr.cruxadvancements.listener;

import killercreepr.crux.core.util.CruxItem;
import killercreepr.cruxadvancements.event.PlayerCraftItemEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
//https://github.com/LMBishop/Quests
public class PlayerCraftItemListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCraftItem(CraftItemEvent event) {
        if ((event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
            || event.getAction() == InventoryAction.NOTHING
            || event.getAction() == InventoryAction.DROP_ONE_SLOT && event.getClick() == ClickType.DROP && (!CruxItem.isEmpty(event.getCursor())) // https://github.com/LMBishop/Quests/issues/430
            || event.getAction() == InventoryAction.DROP_ALL_SLOT && event.getClick() == ClickType.CONTROL_DROP && (!CruxItem.isEmpty(event.getCursor())) // https://github.com/LMBishop/Quests/issues/430
            || event.getAction() == InventoryAction.UNKNOWN && event.getClick() == ClickType.UNKNOWN // for better ViaVersion support
            || !(event.getWhoClicked() instanceof Player player)
            || event.getClick() == ClickType.SWAP_OFFHAND &&
            !CruxItem.isEmpty(player.getInventory().getItemInOffHand())) {
            return;
        }

        ItemStack item = event.getCurrentItem();

        int eventAmount = item.getAmount();
        if (event.isShiftClick() && event.getClick() != ClickType.CONTROL_DROP) { // https://github.com/LMBishop/Quests/issues/317
            int maxAmount = event.getInventory().getMaxStackSize();
            ItemStack[] matrix = event.getInventory().getMatrix();
            for (ItemStack itemStack : matrix) {
                if (itemStack != null && itemStack.getType() != Material.AIR) {
                    int itemStackAmount = itemStack.getAmount();
                    if (itemStackAmount < maxAmount && itemStackAmount > 0) {
                        maxAmount = itemStackAmount;
                    }
                }
            }
            eventAmount *= maxAmount;
            eventAmount = Math.min(eventAmount, getAvailableSpace(player, item));
            if (eventAmount == 0) {
                return;
            }
        }

        ItemStack result = item.clone();
        PlayerCraftItemEvent craftEvent = new PlayerCraftItemEvent(
            event.getView(), event.getSlotType(), event.getSlot(), event.getClick(), event.getAction(),
            event.getHotbarButton(), result, eventAmount
        );
        craftEvent.callEvent();
    }

    public int getAvailableSpace(Player player, ItemStack newItemStack) {
        int availableSpace = 0;
        PlayerInventory inventory = player.getInventory();
        HashMap<Integer, ? extends ItemStack> itemStacksWithSameMaterial = inventory.all(newItemStack.getType());
        for (ItemStack existingItemStack : itemStacksWithSameMaterial.values()) {
            if (newItemStack.isSimilar(existingItemStack)) {
                availableSpace += (newItemStack.getMaxStackSize() - existingItemStack.getAmount());
            }
        }

        for (ItemStack existingItemStack : inventory.getStorageContents()) {
            if (existingItemStack == null) {
                availableSpace += newItemStack.getMaxStackSize();
            }
        }

        return availableSpace;
    }
}
