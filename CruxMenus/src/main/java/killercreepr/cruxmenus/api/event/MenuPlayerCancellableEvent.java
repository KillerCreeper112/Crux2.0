package killercreepr.cruxmenus.api.event;

import killercreepr.cruxmenus.api.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public abstract class MenuPlayerCancellableEvent extends MenuPlayerEvent implements Cancellable {
    protected boolean cancel = false;
    public MenuPlayerCancellableEvent(@NotNull Player who, @NotNull Menu menu) {
        super(who, menu);
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
