package killercreepr.cruxbrewery.recipe;

import net.kyori.adventure.key.Key;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class GenericBrewRecipe implements BrewRecipe{
    protected final @NotNull Key key;
    protected final @NotNull ItemStack ingredient;
    public GenericBrewRecipe(@NotNull Key key, @NotNull ItemStack ingredient) {
        this.key = key;
        this.ingredient = ingredient;
    }

    @Override
    public boolean canBrew(@NotNull BrewerInventory inventory) {
        return ingredient.isSimilar(inventory.getIngredient());
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
