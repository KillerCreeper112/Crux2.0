package killercreepr.cruxblocks.event;

import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.context.BlockContext;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CruxBlockPlaceEvent extends CruxBlockEvent{
    private static final HandlerList HANDLER_LIST = new HandlerList();
    public CruxBlockPlaceEvent(@NotNull CruxBlock block, @NotNull BlockContext context) {
        super(block, context);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
