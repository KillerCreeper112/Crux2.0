package killercreepr.cruxcrafting.api.crafting.recipe;

import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface CruxShapelessRecipe extends CruxCraftingRecipe {
    @NotNull Collection<CruxRecipeIngredient> getIngredients();
}
