package killercreepr.cruxmenus.api.menu.contex;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.item.MenuItem;
import killercreepr.cruxmenus.core.menu.context.SimpleActionContext;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public interface ActionContext extends MenuContext {
    static ActionContext context(@NotNull CfgMenu menu, @NotNull DataExchange info, @NotNull MergedTagContainer resolvers,
                                 @NotNull MenuItem item, @NotNull InventoryClickEvent event){
        return new SimpleActionContext(menu, info, resolvers, item, event);
    }
    @NotNull
    InventoryClickEvent getEvent();
    @NotNull
    MenuItem getItem();
    @NotNull MergedTagContainer getAllMergedResolvers();
}
