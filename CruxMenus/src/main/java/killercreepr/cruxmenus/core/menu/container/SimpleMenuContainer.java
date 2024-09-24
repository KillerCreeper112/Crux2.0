package killercreepr.cruxmenus.core.menu.container;

import killercreepr.cruxmenus.api.event.MenuOpenEvent;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.container.MenuContainer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SimpleMenuContainer implements MenuContainer {
    protected boolean opening = false;
    protected final @NotNull List<Menu> openedMenus = new ArrayList<>();


    public boolean isOpening() {
        return opening;
    }

    public SimpleMenuContainer setOpening(boolean opening) {
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

    public SimpleMenuContainer back(@NotNull Player p){
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
    public SimpleMenuContainer onClosed(@NotNull Player p, @NotNull Menu menu){
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

    public SimpleMenuContainer closeAll(@NotNull Player p){
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

    public SimpleMenuContainer addOpenedMenu(@NotNull MenuOpenEvent menu){
        if(menu.isCancelled()) return this;
        return addOpenedMenu(menu.getMenu());
    }

    public SimpleMenuContainer addOpenedMenu(@NotNull Menu menu){
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
