package killercreepr.cruxmenus.menu.bukkit.api.events.menu;

import killercreepr.cruxmenus.menu.bukkit.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an opened menu is closed.
 */
public class MenuCloseEvent extends MenuPlayerCancellableEvent{
    private static final HandlerList HANDLER_LIST = new HandlerList();
    public MenuCloseEvent(@NotNull Player who, @NotNull Menu menu) {
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
