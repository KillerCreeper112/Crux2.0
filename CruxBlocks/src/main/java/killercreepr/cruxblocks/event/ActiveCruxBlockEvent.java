package killercreepr.cruxblocks.event;

import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.context.BlockContext;
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
