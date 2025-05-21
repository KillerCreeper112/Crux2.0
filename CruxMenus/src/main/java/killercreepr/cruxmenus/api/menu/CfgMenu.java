package killercreepr.cruxmenus.api.menu;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.data.holder.DataInfoHolder;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.core.Crux;
import killercreepr.cruxmenus.api.menu.contex.MenuContext;
import killercreepr.cruxmenus.api.menu.holder.MenuHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItemHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.api.menu.item.MenuItem;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.logging.Level;

public interface CfgMenu extends Menu, DataInfoHolder {
    @NotNull
    MergedTagContainer buildTags();
    void setItems(@NotNull MenuHolder holder);
    void setItems(@NotNull MenuHolder holder, @NotNull MenuContext ctx);
    void setItems(@NotNull MenuItems items);
    void setItems(@NotNull MenuItems items, @NotNull MenuContext ctx);
    default void setItem(int slot, @Nullable MenuItem item, @NotNull Entity viewer){
        setItem(slot, item, viewer, false);
    }

    @ApiStatus.Experimental
    void quickRefresh();

    @ApiStatus.Experimental
    CfgMenu info(@NotNull DataExchange info);

    default void setItem(int slot, @Nullable MenuItem item, @NotNull Entity viewer, boolean silent){
        if(true){//todo
            if(item==null){
                setItem(slot, null, silent);
                return;
            }

            item.buildItemCompletely(viewer).whenComplete((it, throwable) ->{
                /*Crux.scheduler().runTask(task ->{
                });*/
                if (throwable != null) {
                    // An exception occurred
                    Crux.log(Level.WARNING, "Error occurred when building MenuItem: " + throwable.getMessage());
                    return;
                }
                setItem(slot, item, it == null ? null : it.item(), silent);
            });
            return;
        }
        setItem(slot, item, item==null?null:item.buildItem(viewer), silent);
    }

    void setItem(int slot, @Nullable MenuItem item, @Nullable ItemStack display, boolean silent);

    @NotNull
    MenuHolder getHolder();
    @NotNull Map<Integer, MenuItem> getMenuItems();
    default @Nullable MenuItem getMenuItem(int index){
        return getMenuItems().get(index);
    }
    void clearMenuItems(boolean silent);

    @Nullable
    MenuItem setItem(@NotNull MenuHolder holder, int index);
    @Nullable
    MenuItem setItem(@NotNull MenuHolder holder, int index, @NotNull Entity viewer, @NotNull MenuContext menuContext);
    @Nullable
    MenuItem setItem(@NotNull MenuItemHolder menuItem, @NotNull Entity viewer, @NotNull MenuContext menuContext);
    @Nullable
    MenuItem setItem(@NotNull MenuItemHolder menuItem, @NotNull MenuContext menuContext);
}
