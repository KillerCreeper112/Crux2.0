package killercreepr.cruxmenus.menu.bukkit;

import killercreepr.crux.Crux;
import killercreepr.crux.data.DataInfoHolder;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuHolder;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuItemHolder;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuItems;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface CfgMenu extends Menu, DataInfoHolder {
    @NotNull
    MergedTagContainer buildTags();
    void setItems(@NotNull MenuHolder holder);
    void setItems(@NotNull MenuHolder holder, @NotNull MenuContext ctx);
    void setItems(@NotNull MenuItems items);
    void setItems(@NotNull MenuItems items, @NotNull MenuContext ctx);
    default void setItem(int slot, @Nullable MenuItem item, @NotNull Player viewer){
        setItem(slot, item, viewer, false);
    }

    default void setItem(int slot, @Nullable MenuItem item, @NotNull Player viewer, boolean silent){
        if(true){
            if(item==null){
                setItem(slot, null, silent);
                return;
            }
            item.buildItemCompletely(viewer).thenAccept(it ->{
                Crux.getServer().getScheduler().runTask(Crux.getMainPlugin(), task ->{
                    setItem(slot, item, it == null ? null : it.item(), silent);
                });
            });
            return;
        }
        setItem(slot, item, item==null?null:item.buildItem(viewer), silent);
    }

    void setItem(int slot, @Nullable MenuItem item, @Nullable ItemStack display, boolean silent);

    @NotNull MenuHolder getHolder();
    @NotNull Map<Integer, MenuItem> getMenuItems();
    default @Nullable MenuItem getMenuItem(int index){
        return getMenuItems().get(index);
    }
    void clearMenuItems(boolean silent);

    @Nullable MenuItem setItem(@NotNull MenuHolder holder, int index);
    @Nullable MenuItem setItem(@NotNull MenuHolder holder, int index, @NotNull Player viewer, @NotNull MenuContext menuContext);
    @Nullable MenuItem setItem(@NotNull MenuItemHolder menuItem, @NotNull Player viewer, @NotNull MenuContext menuContext);
    @Nullable MenuItem setItem(@NotNull MenuItemHolder menuItem, @NotNull MenuContext menuContext);
}
