package killercreepr.cruxmenu.menu.bukkit.api.events.menu;

import killercreepr.cruxmenu.menu.bukkit.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MenuOpenEvent extends MenuPlayerCancellableEvent{
    private static final HandlerList HANDLER_LIST = new HandlerList();
    public MenuOpenEvent(@NotNull Player who, @NotNull Menu menu) {
        super(who, menu);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
