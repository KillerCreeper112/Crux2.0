package killercreepr.cruxmenus.menu.bukkit.slot;

import killercreepr.crux.util.CruxItem;
import killercreepr.cruxmenus.menu.bukkit.Menu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Slot {
    @NotNull Menu getMenu();
    default @Nullable ItemStack getItem(){
        return getMenu().getInventory().getItem(getIndex());
    }
    default void onMenuUpdate(){
    }
    int getIndex();

    default void onChanged(@NotNull SlotContext ctx){

    }

    default void onMenuOpen(@NotNull Player p){

    }

    default void onMenuClose(@NotNull Player p){

    }

    default @Nullable Integer getMaxStackSize(){
        return null;
    }
    default int getMaxStackSize(@Nullable ItemStack item) {
        Integer maxStack = this.getMaxStackSize();
        if(maxStack==null) return item==null?0:CruxItem.getMaxStackSize(item);
        return Math.min(maxStack, item==null?0:CruxItem.getMaxStackSize(item));
    }

    default boolean mayPlace(@NotNull HumanEntity p, @Nullable ItemStack item){
        return true;
    }

    default boolean mayTake(@NotNull HumanEntity p, @Nullable ItemStack item){
        return true;
    }
}
