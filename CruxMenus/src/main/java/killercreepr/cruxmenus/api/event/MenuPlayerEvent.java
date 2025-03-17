package killercreepr.cruxmenus.api.event;

import killercreepr.crux.core.Crux;
import killercreepr.cruxmenus.api.menu.Menu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class MenuPlayerEvent extends Event {
    protected final @NotNull Menu menu;
    protected final HumanEntity player;
    public MenuPlayerEvent(@NotNull HumanEntity who, @NotNull Menu menu) {
        super(!Crux.isPrimaryThread());
        this.player = who;
        this.menu = menu;
    }

    public @NotNull HumanEntity getPlayer() {
        return player;
    }

    public @NotNull Menu getMenu() {
        return menu;
    }
}
