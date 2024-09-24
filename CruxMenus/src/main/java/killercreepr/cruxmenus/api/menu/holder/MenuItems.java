package killercreepr.cruxmenus.api.menu.holder;

import killercreepr.cruxmenus.core.menu.holder.SimpleMenuItems;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.TreeMap;

public interface MenuItems extends Iterable<Collection<MenuItemHolder>>{
    static MenuItems items(@NotNull TreeMap<Integer, Collection<MenuItemHolder>> items){
        return new SimpleMenuItems(items);
    }
    Collection<MenuItemHolder> get(int index);
    MenuItems add(@NotNull MenuItemHolder item);

    MenuItems add(int priority, @NotNull MenuItemHolder item);
}
