package killercreepr.crux.menu.bukkit.api.events.menu;

import killercreepr.crux.menu.bukkit.Menu;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class MenuEvent extends Event {
    protected final @NotNull Menu menu;
    public MenuEvent(@NotNull Menu menu) {
        this.menu = menu;
    }

    public @NotNull Menu getMenu() {
        return menu;
    }
}
