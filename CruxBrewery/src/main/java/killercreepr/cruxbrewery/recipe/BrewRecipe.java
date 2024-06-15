package killercreepr.cruxbrewery.recipe;

import net.kyori.adventure.key.Keyed;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface BrewRecipe extends Keyed {
    boolean canBrew(@NotNull BrewerInventory inventory);
    void brew(@NotNull BrewerInventory inventory, @NotNull ItemStack item, @NotNull ItemStack ingredient);
    int getBrewTime();
}
