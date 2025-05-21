package killercreepr.cruxmenus.api.event;

import killercreepr.cruxmenus.api.menu.Menu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a menu has been opened for a player.
 */
public class MenuOpenEvent extends MenuEntityCancellableEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    public MenuOpenEvent(@NotNull HumanEntity who, @NotNull Menu menu) {
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
