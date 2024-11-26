package killercreepr.cruxitems.api.event;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.cruxitems.api.item.interaction.ItemUseContext;
import killercreepr.cruxitems.api.item.interaction.ItemUseResult;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CustomItemPreUseEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    protected final @NotNull ItemUseContext context;
    protected @NotNull ItemUseResult useResult;
    public CustomItemPreUseEvent(@NotNull ItemUseContext context, @NotNull ItemUseResult useResult) {
        this.context = context;
        this.useResult = useResult;
    }

    public @NotNull ItemUseResult getUseResult() {
        return useResult;
    }

    public void setUseResult(@NotNull ItemUseResult useResult) {
        this.useResult = useResult;
    }

    public @NotNull CruxItem getItem(){
        return context.getItem();
    }

    public @NotNull ItemUseContext getContext() {
        return context;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
