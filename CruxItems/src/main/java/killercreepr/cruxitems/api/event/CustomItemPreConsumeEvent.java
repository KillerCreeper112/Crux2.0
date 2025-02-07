package killercreepr.cruxitems.api.event;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.cruxitems.api.item.consume.ItemConsumeContext;
import killercreepr.cruxitems.api.item.consume.ItemConsumeResult;
import killercreepr.cruxitems.api.item.interaction.ItemUseContext;
import killercreepr.cruxitems.api.item.interaction.ItemUseResult;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CustomItemPreConsumeEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    protected final @NotNull ItemConsumeContext context;
    protected @NotNull ItemConsumeResult useResult;
    protected boolean cancel = false;
    public CustomItemPreConsumeEvent(@NotNull ItemConsumeContext context, @NotNull ItemConsumeResult useResult) {
        this.context = context;
        this.useResult = useResult;
    }

    public @NotNull ItemConsumeResult getUseResult() {
        return useResult;
    }

    public void setUseResult(@NotNull ItemConsumeResult useResult) {
        this.useResult = useResult;
    }

    public @NotNull CruxItem getItem(){
        return context.getItem();
    }

    public @NotNull ItemConsumeContext getContext() {
        return context;
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
