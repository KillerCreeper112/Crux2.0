package killercreepr.cruxmenu.menu.bukkit.actions;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.tags.container.ObjectStringHookContainer;
import killercreepr.cruxmenu.menu.bukkit.ConfigMenu;
import killercreepr.cruxmenu.menu.bukkit.MenuContext;
import killercreepr.cruxmenu.menu.bukkit.MenuItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class ActionContext extends MenuContext {
    protected final @NotNull MenuItem item;
    protected final @NotNull InventoryClickEvent event;
    public ActionContext(@NotNull ConfigMenu menu, @NotNull DataExchange info, @NotNull ObjectStringHookContainer resolvers,
                         @NotNull MenuItem item, @NotNull InventoryClickEvent event) {
        super(menu, info, resolvers);
        this.item = item;
        this.event = event;
    }

    public @NotNull InventoryClickEvent getEvent() {
        return event;
    }

    public @NotNull MenuItem getItem() {
        return item;
    }
}
