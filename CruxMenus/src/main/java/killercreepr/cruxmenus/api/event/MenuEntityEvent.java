package killercreepr.cruxmenus.api.event;

import killercreepr.crux.core.Crux;
import killercreepr.cruxmenus.api.menu.Menu;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class MenuEntityEvent extends Event {
    protected final @NotNull Menu menu;
    protected final Entity entity;
    public MenuEntityEvent(@NotNull Entity who, @NotNull Menu menu) {
        super(!Crux.isPrimaryThread());
        this.entity = who;
        this.menu = menu;
    }

    public @NotNull Entity getEntity() {
        return entity;
    }

    public @NotNull Menu getMenu() {
        return menu;
    }
}
