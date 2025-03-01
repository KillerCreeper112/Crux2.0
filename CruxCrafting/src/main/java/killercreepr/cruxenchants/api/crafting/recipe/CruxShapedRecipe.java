package killercreepr.cruxenchants.api.crafting.recipe;

import killercreepr.cruxenchants.api.crafting.CruxCraftingMatrix;
import killercreepr.cruxenchants.api.crafting.ingredient.CruxRecipeIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface CruxShapedRecipe extends CruxCraftingRecipe {
    @NotNull Map<Integer, CruxRecipeIngredient> getIngredients();
    int getWidth();
    int getHeight();
    @NotNull Map<Integer, CruxRecipeIngredient> mapIngredientsToMatrix(@NotNull CruxCraftingMatrix matrix, int offsetX, int offsetY, boolean flipped);
}
