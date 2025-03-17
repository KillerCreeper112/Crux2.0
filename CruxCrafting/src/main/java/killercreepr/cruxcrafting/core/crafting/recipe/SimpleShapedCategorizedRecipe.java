package killercreepr.cruxcrafting.core.crafting.recipe;

import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.cruxcrafting.api.crafting.RecipeCategory;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import killercreepr.cruxcrafting.api.crafting.recipe.CategorizedRecipe;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SimpleShapedCategorizedRecipe extends SimpleShapedRecipe implements CategorizedRecipe {
    protected final Collection<RecipeCategory> category;
    public SimpleShapedCategorizedRecipe(Key key, Map<Integer, CruxRecipeIngredient> ingredients, List<DynamicItem> results, int width, int height, Collection<RecipeCategory> category) {
        super(key, ingredients, results, width, height);
        this.category = category;
    }

    @Override
    public @NotNull Collection<RecipeCategory> getRecipeCategories() {
        return category;
    }
}
