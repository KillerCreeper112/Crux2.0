package killercreepr.crux.menu.actions;

import killerceepr.crux.data.DataExchange;
import killerceepr.crux.menu.ConfigMenu;
import killerceepr.crux.menu.MenuInfo;
import killerceepr.crux.menu.MenuItem;
import killerceepr.crux.tags.container.ObjectStringHookContainer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class ActionInfo extends MenuInfo {
    private final @NotNull MenuItem item;
    private final @NotNull InventoryClickEvent event;

    public ActionInfo(@NotNull ConfigMenu menu, @NotNull DataExchange info, @NotNull ObjectStringHookContainer resolvers,
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
