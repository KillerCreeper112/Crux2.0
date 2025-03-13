package killercreepr.cruxcrafting.api.crafting.recipe;

import killercreepr.cruxcrafting.api.crafting.RecipeCategory;
import org.jetbrains.annotations.NotNull;

public interface CategorizedRecipe extends CruxRecipe{
    @NotNull RecipeCategory getRecipeCategory();
}
