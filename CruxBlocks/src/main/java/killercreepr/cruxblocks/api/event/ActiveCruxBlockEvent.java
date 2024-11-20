package killercreepr.cruxblocks.api.event;

import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.api.block.context.BlockContext;
import org.jetbrains.annotations.NotNull;

public abstract class ActiveCruxBlockEvent extends CruxBlockEvent {
    protected final @NotNull ActiveCruxBlock active;
    public ActiveCruxBlockEvent(@NotNull CruxBlock block, @NotNull BlockContext context, @NotNull ActiveCruxBlock active) {
        super(block, context);
        this.active = active;
    }
    public @NotNull ActiveCruxBlock getActive() {
        return active;
    }
}
