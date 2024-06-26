package killercreepr.cruxmenus.menu.bukkit;

import killercreepr.cruxmenus.menu.bukkit.api.events.menu.MenuOpenEvent;
import org.bukkit.Bukkit;
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

    public MenuOpenEvent next(@NotNull Supplier<Menu> menu, @NotNull Player p){
        return next(() -> menu.get().open(p));
    }

    public MenuOpenEvent next(@NotNull Supplier<MenuOpenEvent> open){
        setOpening(true);
        MenuOpenEvent event = open.get();
        addOpenedMenu(event);
        setOpening(false);
        return event;
    }

    public MenuOpenEvent back(@NotNull Supplier<Menu> menu, @NotNull Player p){
        return back(() ->{
            Menu m = menu.get();
            m.refresh();
            return m.open(p);
        });
    }

    public MenuOpenEvent back(@NotNull Supplier<MenuOpenEvent> open){
        setOpening(true);
        MenuOpenEvent event = open.get();
        if(!event.isCancelled()) removeCurrent();
        setOpening(false);
        return event;
    }

    public MenuContainer back(@NotNull Player p){
        Menu menu = getPrevious();
        if(menu==null) return this;
        back(() -> menu, p);
        return this;
    }

    /**
     * Calls the onClose(Player) method on all menus except for
     * the current menu.
     */
    protected boolean closed = false;
    public MenuContainer onClosed(@NotNull Player p, @NotNull Menu menu){
        if(isClosed() || isOpening()) return this;
        closed = true;
        for(Menu m : openedMenus){
            if(m.equals(menu)) continue;
            m.onClose(p);
        }
        removeCurrent();
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
        int index = 0;
        int size = openedMenus.size();
        for(Menu m : openedMenus){
            index++;
            if(index==size) continue;
            m.onClose(p);
        }
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

    public @Nullable Menu removeCurrent(){
        return removeOpenedMenu(openedMenus.size()-1);
    }

    public @Nullable Menu getOpenedMenu(int index){
        if(index < 0 || index >= openedMenus.size()) return null;
        return openedMenus.get(index);
    }

    public @Nullable Menu removeOpenedMenu(int index){
        Bukkit.broadcastMessage("removing at " + index + " - " + getOpenedMenu(index));
        if(index < 0 || index >= openedMenus.size()) return null;
        return openedMenus.remove(index);
    }

    public @NotNull List<Menu> getOpenedMenus() {
        return openedMenus;
    }

    public @Nullable Menu getCurrent() {
        return openedMenus.isEmpty()? null : openedMenus.getLast();
    }

    public @Nullable Menu getPrevious(){
        return getOpenedMenu(openedMenus.size()-2);
    }
}
