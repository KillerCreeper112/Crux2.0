package killercreepr.cruxitems.item.inventory.impl;

import killercreepr.crux.core.util.CruxItem;
import killercreepr.cruxitems.item.inventory.ItemClickContext;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemClickContextImpl implements ItemClickContext {
    public static Builder builder(){
        return new Builder();
    }

    protected final @NotNull HumanEntity whoClicked;
    protected final @NotNull CruxItem item;
    protected final @Nullable Inventory clickedInventory;
    protected final int slot;
    protected final int rawSlot;
    protected final int hotbarButton;
    protected final @NotNull InventoryAction action;
    protected final @NotNull ClickType clickType;

    public ItemClickContextImpl(@NotNull HumanEntity whoClicked, @NotNull CruxItem item, @Nullable Inventory clickedInventory, int slot, int rawSlot, int hotbarButton, @NotNull InventoryAction action, @NotNull ClickType clickType) {
        this.whoClicked = whoClicked;
        this.item = item;
        this.clickedInventory = clickedInventory;
        this.slot = slot;
        this.rawSlot = rawSlot;
        this.hotbarButton = hotbarButton;
        this.action = action;
        this.clickType = clickType;
    }

    @Override
    public @NotNull HumanEntity getWhoClicked() {
        return whoClicked;
    }

    @Override
    public @NotNull CruxItem getItem() {
        return item;
    }

    @Override
    public @Nullable Inventory getClickedInventory() {
        return clickedInventory;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public int getRawSlot() {
        return rawSlot;
    }

    @Override
    public int getHotbarButton() {
        return hotbarButton;
    }

    @Override
    public @NotNull InventoryAction getAction() {
        return action;
    }

    @Override
    public @NotNull ClickType getClick() {
        return clickType;
    }

    public static final class Builder {
        private @NotNull HumanEntity whoClicked;
        private @NotNull CruxItem item;
        private @Nullable Inventory clickedInventory;
        private int slot;
        private int rawSlot;
        private int hotbarButton;
        private @NotNull InventoryAction action;
        private @NotNull ClickType clickType;

        private Builder() {
        }

        public static Builder anItemClickContextImpl() {
            return new Builder();
        }

        public Builder whoClicked(HumanEntity whoClicked) {
            this.whoClicked = whoClicked;
            return this;
        }

        public Builder item(CruxItem item) {
            this.item = item;
            return this;
        }

        public Builder clickedInventory(Inventory clickedInventory) {
            this.clickedInventory = clickedInventory;
            return this;
        }

        public Builder slot(int slot) {
            this.slot = slot;
            return this;
        }

        public Builder rawSlot(int rawSlot) {
            this.rawSlot = rawSlot;
            return this;
        }

        public Builder hotbarButton(int hotbarButton) {
            this.hotbarButton = hotbarButton;
            return this;
        }

        public Builder action(InventoryAction action) {
            this.action = action;
            return this;
        }

        public Builder clickType(ClickType clickType) {
            this.clickType = clickType;
            return this;
        }

        public ItemClickContextImpl build() {
            return new ItemClickContextImpl(whoClicked, item, clickedInventory, slot, rawSlot, hotbarButton, action, clickType);
        }
    }
}
