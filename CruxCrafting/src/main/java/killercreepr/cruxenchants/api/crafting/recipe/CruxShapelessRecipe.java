package killercreepr.cruxenchants.api.crafting.recipe;

import killercreepr.cruxenchants.api.crafting.ingredient.CruxRecipeIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface CruxShapelessRecipe extends CruxCraftingRecipe {
    @NotNull Collection<CruxRecipeIngredient> getIngredients();
}
