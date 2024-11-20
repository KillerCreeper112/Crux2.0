package killercreepr.cruxblocks.api.event;

import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.context.BlockContext;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class CruxBlockEvent extends Event {
    protected final @NotNull CruxBlock block;
    protected @NotNull BlockContext context;
    public CruxBlockEvent(@NotNull CruxBlock block, @NotNull BlockContext context) {
        this.block = block;
        this.context = context;
    }

    public CruxBlockEvent(boolean isAsync, @NotNull CruxBlock block, @NotNull BlockContext context) {
        super(isAsync);
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
