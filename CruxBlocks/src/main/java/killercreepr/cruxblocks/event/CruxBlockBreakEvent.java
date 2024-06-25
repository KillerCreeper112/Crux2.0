package killercreepr.cruxblocks.event;

import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.context.BlockContext;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class CruxBlockBreakEvent extends ActiveCruxBlockEvent{
    private static final HandlerList HANDLER_LIST = new HandlerList();
    protected @Nullable Collection<ItemStack> drops;
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
}
