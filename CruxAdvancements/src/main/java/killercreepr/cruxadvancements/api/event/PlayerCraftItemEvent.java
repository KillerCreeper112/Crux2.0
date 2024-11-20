package killercreepr.cruxadvancements.api.event;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerCraftItemEvent extends InventoryClickEvent {
    protected final @NotNull ItemStack result;
    protected final int resultAmount;
    public PlayerCraftItemEvent(@NotNull InventoryView view, @NotNull InventoryType.SlotType type, int slot,
                                @NotNull ClickType click, @NotNull InventoryAction action, @NotNull ItemStack result, int resultAmount) {
        super(view, type, slot, click, action);
        this.result = result;
        this.resultAmount = resultAmount;
    }

    public PlayerCraftItemEvent(@NotNull InventoryView view, @NotNull InventoryType.SlotType type, int slot,
                                @NotNull ClickType click, @NotNull InventoryAction action, int hotbarKey, @NotNull ItemStack result, int resultAmount) {
        super(view, type, slot, click, action, hotbarKey);
        this.result = result;
        this.resultAmount = resultAmount;
    }

    public @NotNull ItemStack getItemResult() {
        return result;
    }

    public int getResultAmount() {
        return resultAmount;
    }
}
