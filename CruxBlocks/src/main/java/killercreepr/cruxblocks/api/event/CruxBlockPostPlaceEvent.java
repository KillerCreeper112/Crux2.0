package killercreepr.cruxblocks.api.event;

import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.context.BlockContext;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CruxBlockPostPlaceEvent extends CruxBlockEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    //protected boolean cancel = false;
    public CruxBlockPostPlaceEvent(@NotNull CruxBlock block, @NotNull BlockContext context) {
        super(block, context);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
