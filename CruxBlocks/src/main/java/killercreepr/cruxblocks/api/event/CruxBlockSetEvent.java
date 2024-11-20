package killercreepr.cruxblocks.api.event;

import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.context.BlockContext;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CruxBlockSetEvent extends CruxBlockEvent{
    private static final HandlerList HANDLER_LIST = new HandlerList();
    public CruxBlockSetEvent(@NotNull CruxBlock block, @NotNull BlockContext context) {
        super(block, context);
    }

    public CruxBlockSetEvent(boolean isAsync, @NotNull CruxBlock block, @NotNull BlockContext context) {
        super(isAsync, block, context);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
