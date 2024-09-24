package killercreepr.cruxmenus.api.menu.contex;

import killercreepr.cruxmenus.core.menu.slot.SimpleSlotContext;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SlotContext extends Cancellable {
    static SlotContext context(@NotNull ItemStack newItem, @NotNull ItemStack oldItem){
        return new SimpleSlotContext(newItem, oldItem);
    }
    @Nullable ItemStack getOldItem();

    @Nullable ItemStack getNewItem();
}
