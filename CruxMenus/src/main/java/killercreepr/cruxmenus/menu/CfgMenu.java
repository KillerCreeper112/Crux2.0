package killercreepr.cruxmenus.menu;

import killercreepr.crux.data.DataInfoHolder;
import killercreepr.cruxmenus.menu.bukkit.Menu;
import killercreepr.cruxmenus.menu.bukkit.MenuItem;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface CfgMenu extends Menu, DataInfoHolder {
    void setItems(@NotNull MenuHolder holder);
    default void setItem(int slot, @Nullable MenuItem item, @NotNull Player viewer){
        setItem(slot, item, viewer, false);
    }

    default void setItem(int slot, @Nullable MenuItem item, @NotNull Player viewer, boolean silent){
        setItem(slot, item, item==null?null:item.buildItem(viewer), silent);
    }

    void setItem(int slot, @Nullable MenuItem item, @Nullable ItemStack display, boolean silent);

    @NotNull MenuHolder getHolder();
    @NotNull Map<Integer, MenuItem> getMenuItems();
    void clearMenuItems(boolean silent);
}
