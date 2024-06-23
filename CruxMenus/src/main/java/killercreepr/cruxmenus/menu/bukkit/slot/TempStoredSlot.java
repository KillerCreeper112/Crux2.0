package killercreepr.cruxmenus.menu.bukkit.slot;

import killercreepr.crux.util.CruxEntity;
import killercreepr.crux.util.CruxItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface TempStoredSlot extends Slot{
    @Override
    default void onMenuClose(@NotNull Player p) {
        ItemStack item = getItem();
        if(CruxItem.isEmpty(item) || isSlottedItem(item)) return;
        CruxEntity.giveOrDrop(p, item);
    }
}
