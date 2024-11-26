package killercreepr.cruxitems.api.event;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.cruxitems.api.item.component.InteractableComponent;
import killercreepr.cruxitems.api.item.interaction.ItemUseContext;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class InteractableComponentUseEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    protected final @NotNull ItemUseContext context;
    protected final @NotNull InteractableComponent component;
    protected boolean cancel = false;
    public InteractableComponentUseEvent(@NotNull ItemUseContext context, @NotNull InteractableComponent component) {
        this.context = context;
        this.component = component;
    }

    public @NotNull CruxItem getItem(){
        return context.getItem();
    }

    public @NotNull ItemUseContext getContext() {
        return context;
    }

    public @NotNull InteractableComponent getComponent() {
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
