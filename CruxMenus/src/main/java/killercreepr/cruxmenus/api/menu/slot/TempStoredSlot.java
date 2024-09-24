package killercreepr.cruxmenus.api.menu.slot;

import killercreepr.crux.util.CruxEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface TempStoredSlot extends Slot, TempSlotted {
    @Override
    default void onMenuClose(@NotNull Player p) {
        if(!giveItemUponClose()) return;
        ItemStack item = getItem();
        if(isBlank(item) || item == null) return;
        CruxEntity.giveOrDrop(p, item);
    }

    @Override
    default boolean giveItemUponClose(){
        if(getMenu() instanceof TempSlotted s) return s.giveItemUponClose();
        return true;
    }
}
