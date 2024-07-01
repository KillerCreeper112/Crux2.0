package killercreepr.cruxmenus.menu.bukkit.actions;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.cruxmenus.menu.bukkit.ConfigMenu;
import killercreepr.cruxmenus.menu.bukkit.MenuContext;
import killercreepr.cruxmenus.menu.bukkit.MenuItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class ActionContext extends MenuContext {
    protected final @NotNull MenuItem item;
    protected final @NotNull InventoryClickEvent event;
    public ActionContext(@NotNull ConfigMenu menu, @NotNull DataExchange info, @NotNull MergedTagContainer resolvers,
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

    @Override
    public @NotNull MergedTagContainer getAllMergedResolvers() {
        return super.getAllMergedResolvers().addAll(item.buildTags());
    }
}
