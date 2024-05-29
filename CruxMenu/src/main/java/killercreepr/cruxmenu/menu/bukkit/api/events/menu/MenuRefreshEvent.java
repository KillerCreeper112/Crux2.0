package killercreepr.cruxmenu.menu.bukkit.api.events.menu;

import killercreepr.cruxmenu.menu.bukkit.Menu;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MenuRefreshEvent extends MenuEvent{
    private static final HandlerList HANDLER_LIST = new HandlerList();
    public MenuRefreshEvent(@NotNull Menu menu) {
        super(menu);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
