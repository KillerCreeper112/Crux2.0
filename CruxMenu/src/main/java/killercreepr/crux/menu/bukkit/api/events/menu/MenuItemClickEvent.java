package killercreepr.crux.menu.bukkit.api.events.menu;

import killercreepr.crux.menu.bukkit.Menu;
import killercreepr.crux.menu.bukkit.MenuItem;
import killercreepr.crux.menu.bukkit.actions.ActionContext;
import killercreepr.crux.menu.bukkit.holder.ClickActions;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
