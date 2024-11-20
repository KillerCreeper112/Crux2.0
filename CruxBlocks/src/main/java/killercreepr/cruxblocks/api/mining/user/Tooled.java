package killercreepr.cruxblocks.api.mining.user;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface Tooled {
    @Nullable
    ItemStack getTool();
}
