package killercreepr.cruxmenus.core.menu.slot;

import killercreepr.cruxmenus.api.menu.contex.SlotContext;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleSlotContext implements SlotContext {
    protected final HumanEntity whoClicked;
    protected @Nullable ItemStack oldItem;
    protected @Nullable ItemStack newItem;
    protected boolean cancel = false;

    public SimpleSlotContext(HumanEntity whoClicked, @Nullable ItemStack newItem, @Nullable ItemStack oldItem) {
        this.whoClicked = whoClicked;
        this.newItem = newItem;
        this.oldItem = oldItem;
    }

    public @Nullable ItemStack getOldItem() {
        return oldItem;
    }

    public @Nullable ItemStack getNewItem() {
        return newItem;
    }

    @Override
    public @NotNull HumanEntity getWhoClicked() {
        return whoClicked;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
