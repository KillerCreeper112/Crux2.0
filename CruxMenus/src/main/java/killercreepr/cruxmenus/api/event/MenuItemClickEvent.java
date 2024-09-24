package killercreepr.cruxmenus.api.event;

import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.action.click.ClickActions;
import killercreepr.cruxmenus.api.menu.contex.ActionContext;
import killercreepr.cruxmenus.api.menu.item.MenuItem;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a MenuItem in a menu is clicked on.
 */
public class MenuItemClickEvent extends MenuPlayerCancellableEvent{
    private static final HandlerList HANDLER_LIST = new HandlerList();

    protected final @NotNull MenuItem item;
    protected @NotNull ActionContext context;
    protected @Nullable ClickActions clickActions;

    public MenuItemClickEvent(@NotNull Player who, @NotNull Menu menu, @NotNull MenuItem item, @NotNull ActionContext context, @Nullable ClickActions clickActions) {
        super(who, menu);
        this.item = item;
        this.context = context;
        this.clickActions = clickActions;
    }

    public @NotNull MenuItem getItem() {
        return item;
    }

    public @NotNull ActionContext getContext() {
        return context;
    }

    public void setContext(@NotNull ActionContext context) {
        this.context = context;
    }

    public @Nullable ClickActions getClickActions() {
        return clickActions;
    }

    public void setClickActions(@Nullable ClickActions clickActions) {
        this.clickActions = clickActions;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
