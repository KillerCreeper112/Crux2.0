package killercreepr.cruxmenus.api.menu.container;

import killercreepr.cruxmenus.api.event.MenuOpenEvent;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.core.menu.container.SimpleMenuContainer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public interface MenuContainer {
    static MenuContainer createNew(){
        return new SimpleMenuContainer();
    }

    boolean isOpening();

    MenuContainer setOpening(boolean opening);

    MenuOpenEvent next(@NotNull Supplier<Menu> menu, @NotNull Player p);

    MenuOpenEvent next(@NotNull Supplier<MenuOpenEvent> open);

    MenuOpenEvent back(@NotNull Supplier<Menu> menu, @NotNull Player p);

    MenuOpenEvent back(@NotNull Supplier<MenuOpenEvent> open);

    MenuContainer back(@NotNull Player p);

    /**
     * Calls the onClose(Player) method on all menus except for
     * the current menu.
     */
    MenuContainer onClosed(@NotNull Player p, @NotNull Menu menu);

    boolean isClosed();

    void setClosed(boolean closed);

    MenuContainer closeAll(@NotNull Player p);

    MenuContainer addOpenedMenu(@NotNull MenuOpenEvent menu);

    MenuContainer addOpenedMenu(@NotNull Menu menu);

    @Nullable Menu removeCurrent();

    @Nullable Menu getOpenedMenu(int index);

    @Nullable Menu removeOpenedMenu(int index);

    @NotNull List<Menu> getOpenedMenus();

    @Nullable Menu getCurrent();

    @Nullable Menu getPrevious();
}
