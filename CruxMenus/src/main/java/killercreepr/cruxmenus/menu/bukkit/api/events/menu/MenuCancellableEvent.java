package killercreepr.cruxmenus.menu.bukkit.api.events.menu;

import killercreepr.cruxmenus.menu.bukkit.Menu;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public abstract class MenuCancellableEvent extends MenuEvent implements Cancellable {
    protected boolean cancel = false;
    public MenuCancellableEvent(@NotNull Menu menu) {
        super(menu);
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
