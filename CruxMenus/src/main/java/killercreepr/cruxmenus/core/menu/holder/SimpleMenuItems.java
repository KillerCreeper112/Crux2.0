package killercreepr.cruxmenus.core.menu.holder;

import killercreepr.cruxmenus.api.menu.holder.MenuItemHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SimpleMenuItems implements MenuItems {
    private final TreeMap<Integer, Collection<MenuItemHolder>> items;
    public SimpleMenuItems(@NotNull TreeMap<Integer, Collection<MenuItemHolder>> items) {
        this.items = items;
    }

    public MenuItems add(@NotNull MenuItemHolder item){
        int priority = item.info().getObject("priority", Number.class).orElse(0).intValue();
        return add(priority, item);
    }

    public MenuItems add(int priority, @NotNull MenuItemHolder item){
        if(items.containsKey(priority)){
            items.get(priority).add(item);
            return this;
        }
        Collection<MenuItemHolder> list = new HashSet<>();
        list.add(item);
        items.put(priority, list);
        return this;
    }

    public @NotNull TreeMap<Integer, Collection<MenuItemHolder>> getItems() {
        return items;
    }

    public @NotNull List<MenuItemHolder> items(){
        List<MenuItemHolder> items = new ArrayList<>();
        this.items.values().forEach(items::addAll);
        return items;
    }

    @NotNull
    @Override
    public Iterator<Collection<MenuItemHolder>> iterator() {
        return items.values().iterator();
    }

    @Override
    public Collection<MenuItemHolder> get(int index) {
        return items.get(index);
    }
}
