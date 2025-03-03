package killercreepr.cruxcrafting.api.crafting.recipe;

import killercreepr.cruxcrafting.api.crafting.CruxCraftingMatrix;
import killercreepr.cruxcrafting.api.crafting.CruxRecipeResult;
import killercreepr.cruxcrafting.api.crafting.context.CruxCraftingRecipeContext;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface CruxCraftingRecipe extends CruxRecipe {
    @NotNull Map<Integer, CruxRecipeIngredient> mapIngredients(@NotNull CruxCraftingMatrix matrix);
    @NotNull Map<Integer, CruxRecipeIngredient> mapIngredientDisplay();
    @NotNull Map<Integer, CruxRecipeIngredient> mapIngredientDisplay(int width, int height);
    /**
     * Only used for showcasing what items this recipe will return.
     */
    @NotNull List<ItemStack> getDisplayedResultItems();
    @Nullable CruxRecipeResult testResult(@NotNull CruxCraftingRecipeContext ctx);
}
