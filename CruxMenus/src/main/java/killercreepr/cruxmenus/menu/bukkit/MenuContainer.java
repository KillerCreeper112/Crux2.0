package killercreepr.cruxmenus.menu.bukkit;

import killercreepr.cruxmenus.menu.bukkit.api.events.menu.MenuOpenEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MenuContainer {
    protected boolean opening = false;
    protected final @NotNull List<Menu> openedMenus = new ArrayList<>();


    public boolean isOpening() {
        return opening;
    }

    public MenuContainer setOpening(boolean opening) {
        this.opening = opening; return this;
    }

    public MenuOpenEvent open(@NotNull Supplier<Menu> menu, @NotNull Player p){
        setOpening(true);
        MenuOpenEvent event = menu.get().open(p);
        addOpenedMenu(event);
        setOpening(false);
        return event;
    }

    public MenuOpenEvent open(@NotNull Supplier<MenuOpenEvent> open){
        setOpening(true);
        MenuOpenEvent event = open.get();
        addOpenedMenu(event);
        setOpening(false);
        return event;
    }

    /**
     * Calls the onClose(Player) method on all menus except for
     * the current menu.
     */
    protected boolean closed = false;
    public MenuContainer closed(@NotNull Player p){
        if(closed) return this;
        closed = true;
        int index = -1;
        for(Menu m : openedMenus){
            index++;
            if(index==0) continue;
            m.onClose(p);
        }
        return this;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public MenuContainer closeAll(@NotNull Player p){
        Menu opened = getCurrent();
        if(opened==null) return this;
        opened.close(p);
        int index = -1;
        for(Menu m : openedMenus){
            index++;
            if(index==0) continue;
            m.onClose(p);
        }
        return this;
    }

    public MenuContainer back(@NotNull Player p){
        int index = openedMenus.size()-1;
        Menu menu = getOpenedMenu(index);
        if(menu==null) return this;
        menu.open(p);
        return this;
    }

    public MenuContainer next(@NotNull Player p, @NotNull Menu menu){
        MenuOpenEvent event = menu.open(p);
        if(event.isCancelled()) return this;
        addOpenedMenu(menu);
        return this;
    }

    public MenuContainer addOpenedMenu(@NotNull MenuOpenEvent menu){
        if(menu.isCancelled()) return this;
        return addOpenedMenu(menu.getMenu());
    }

    public MenuContainer addOpenedMenu(@NotNull Menu menu){
        openedMenus.add(menu);
        return this;
    }

    public @Nullable Menu getOpenedMenu(int index){
        if(index >= openedMenus.size()) return null;
        return openedMenus.get(index);
    }

    public @Nullable Menu removeOpenedMenu(int index){
        if(index >= openedMenus.size()) return null;
        return openedMenus.remove(index);
    }

    public @NotNull List<Menu> getOpenedMenus() {
        return openedMenus;
    }

    public @Nullable Menu getCurrent() {
        return openedMenus.isEmpty()? null : openedMenus.getFirst();
    }
}
