package killercreepr.cruxitems.api.event;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.cruxitems.api.item.component.ConsumableComponent;
import killercreepr.cruxitems.api.item.consume.ItemConsumeContext;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ConsumableComponentUseEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    protected final @NotNull ItemConsumeContext context;
    protected final @NotNull ConsumableComponent component;
    protected boolean cancel = false;
    public ConsumableComponentUseEvent(@NotNull ItemConsumeContext context, @NotNull ConsumableComponent component) {
        this.context = context;
        this.component = component;
    }

    public @NotNull CruxItem getItem(){
        return context.getItem();
    }

    public @NotNull ItemConsumeContext getContext() {
        return context;
    }

    public @NotNull ConsumableComponent getComponent() {
        return component;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
