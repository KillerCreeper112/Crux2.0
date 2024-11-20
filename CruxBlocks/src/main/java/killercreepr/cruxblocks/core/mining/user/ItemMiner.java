package killercreepr.cruxblocks.core.mining.user;

import killercreepr.cruxblocks.api.mining.user.Miner;
import killercreepr.cruxblocks.api.mining.user.Tooled;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemMiner implements Tooled, Miner {
    protected final @Nullable ItemStack tool;
    public ItemMiner(@Nullable ItemStack tool) {
        this.tool = tool;
    }

    @Override
    public @Nullable ItemStack getTool() {
        return tool;
    }

    @Override
    public Object getHandle() {
        return tool;
    }
}
