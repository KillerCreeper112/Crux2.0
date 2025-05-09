package killercreepr.cruxitems.api.item.inventory;

import killercreepr.cruxitems.api.item.CruxedItem;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ItemClickContext {
    @NotNull
    HumanEntity getWhoClicked();
    @NotNull
    CruxedItem getItem();
    @Nullable
    Inventory getClickedInventory();
    int getSlot();
    int getRawSlot();
    int getHotbarButton();
    @NotNull
    InventoryAction getAction();
    @NotNull
    ClickType getClick();
    @NotNull InventoryView getView();
}
