package killercreepr.cruxblocks.event;

import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.context.BlockContext;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class CruxBlockEvent extends Event {
    protected final @NotNull CruxBlock block;
    protected @NotNull BlockContext context;
    public CruxBlockEvent(@NotNull CruxBlock block, @NotNull BlockContext context) {
        this.block = block;
        this.context = context;
    }

    public @NotNull CruxBlock getBlock() {
        return block;
    }

    public @NotNull BlockContext getContext() {
        return context;
    }

    @Deprecated(forRemoval = true, since = "Probably not good idea...")
    public void setContext(@NotNull BlockContext context) {
        this.context = context;
    }
}
