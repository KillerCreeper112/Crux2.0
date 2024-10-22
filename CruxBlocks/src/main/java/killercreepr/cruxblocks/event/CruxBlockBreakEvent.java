package killercreepr.cruxblocks.event;

import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.context.BlockContext;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class CruxBlockBreakEvent extends ActiveCruxBlockEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    protected @Nullable Collection<ItemStack> drops;
    protected boolean cancel = false;
    public CruxBlockBreakEvent(@NotNull ActiveCruxBlock block, @NotNull BlockContext context, @Nullable Collection<ItemStack> drops) {
        super(block.getCruxBlock(), context, block);
        this.drops = drops;
    }

    public @Nullable Collection<ItemStack> getDrops() {
        return drops;
    }

    public void setDrops(@Nullable Collection<ItemStack> drops) {
        this.drops = drops;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
