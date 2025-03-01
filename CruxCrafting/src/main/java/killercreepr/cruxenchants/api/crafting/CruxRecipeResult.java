package killercreepr.cruxenchants.api.crafting;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CruxRecipeResult {
    @NotNull
    List<ItemStack> getResultItems();
}
