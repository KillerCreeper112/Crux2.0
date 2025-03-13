package killercreepr.cruxcrafting.api.crafting.recipe;

import killercreepr.cruxcrafting.api.crafting.RecipeCategory;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface CategorizedRecipe extends CruxRecipe{
    @NotNull Collection<RecipeCategory> getRecipeCategories();
}
