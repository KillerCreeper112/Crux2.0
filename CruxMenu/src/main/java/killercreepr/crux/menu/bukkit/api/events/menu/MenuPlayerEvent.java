package killercreepr.crux.menu.bukkit.api.events.menu;

import killercreepr.crux.menu.bukkit.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public abstract class MenuPlayerEvent extends PlayerEvent {
    protected final @NotNull Menu menu;
    public MenuPlayerEvent(@NotNull Player who, @NotNull Menu menu) {
        super(who);
        this.menu = menu;
    }

    public @NotNull Menu getMenu() {
        return menu;
    }
}
