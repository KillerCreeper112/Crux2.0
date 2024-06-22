package killercreepr.cruxmenus.menu.bukkit.slot;

import killercreepr.crux.util.CruxItem;
import killercreepr.cruxmenus.menu.bukkit.Menu;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Slot {
    @NotNull Menu getMenu();
    default void onMenuChanged(){}
    int getIndex();

    default void onChanged(@NotNull SlotContext ctx){
    }

    default @Nullable Integer getMaxStackSize(){
        return null;
    }
    default int getMaxStackSize(@Nullable ItemStack item) {
        Integer maxStack = this.getMaxStackSize();
        if(maxStack==null) return item==null?0:CruxItem.getMaxStackSize(item);
        return Math.min(maxStack, item==null?0:CruxItem.getMaxStackSize(item));
    }
    default boolean mayPlace(@Nullable ItemStack item){
        return true;
    }
}
