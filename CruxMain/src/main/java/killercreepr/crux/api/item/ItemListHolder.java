package killercreepr.crux.api.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ItemListHolder {
    @NotNull List<ItemStack> getItemValues();
}
