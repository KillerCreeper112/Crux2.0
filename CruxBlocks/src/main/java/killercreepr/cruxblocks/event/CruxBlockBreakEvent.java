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
    protected final @Nullable ItemStack tool;
    public CruxBlockBreakEvent(@NotNull ActiveCruxBlock block, @NotNull BlockContext context, @Nullable Collection<ItemStack> drops, @Nullable ItemStack tool) {
        super(block.getCruxBlock(), context, block);
        this.drops = drops;
        this.tool = tool;
    }

    public @Nullable Collection<ItemStack> getDrops() {
        return drops;
    }

    public void setDrops(@Nullable Collection<ItemStack> drops) {
        this.drops = drops;
    }

    public @Nullable ItemStack getTool() {
        return tool;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
