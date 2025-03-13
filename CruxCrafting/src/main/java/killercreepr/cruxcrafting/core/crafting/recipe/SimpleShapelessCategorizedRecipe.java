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

public class SimpleShapelessCategorizedRecipe extends SimpleShapelessRecipe implements CategorizedRecipe {
    protected final RecipeCategory category;

    public SimpleShapelessCategorizedRecipe(Key key, Collection<CruxRecipeIngredient> ingredients, List<DynamicItem> results, RecipeCategory category) {
        super(key, ingredients, results);
        this.category = category;
    }


    @Override
    public @NotNull RecipeCategory getRecipeCategory() {
        return category;
    }
}
