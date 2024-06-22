package killercreepr.cruxmenus.menu.bukkit.slot;

import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SlotContext implements Cancellable {
    protected @Nullable ItemStack oldItem;
    protected @Nullable ItemStack newItem;
    protected boolean cancel = false;

    public SlotContext(@Nullable ItemStack newItem, @Nullable ItemStack oldItem) {
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
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
