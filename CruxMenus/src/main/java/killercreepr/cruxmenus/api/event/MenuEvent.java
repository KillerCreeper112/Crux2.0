package killercreepr.cruxmenus.api.event;

import killercreepr.crux.core.Crux;
import killercreepr.cruxmenus.api.menu.Menu;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class MenuEvent extends Event {
    protected final @NotNull Menu menu;
    public MenuEvent(@NotNull Menu menu) {
        super(!Crux.isPrimaryThread());
        this.menu = menu;
    }

    public @NotNull Menu getMenu() {
        return menu;
    }
}
