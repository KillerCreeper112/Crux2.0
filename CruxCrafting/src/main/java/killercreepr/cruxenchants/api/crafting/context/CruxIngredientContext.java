package killercreepr.cruxenchants.api.crafting.context;

import killercreepr.cruxenchants.core.crafting.context.SimpleIngredientContext;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxIngredientContext {
    static RecipeContexted ingredientContext(@Nullable ItemStack ingredient, @NotNull CruxRecipeContext recipeContext){
        return new SimpleIngredientContext.RecipeContexted(ingredient, recipeContext);
    }

    static CruxIngredientContext ingredientContext(@Nullable ItemStack ingredient){
        return new SimpleIngredientContext(ingredient);
    }

    @Nullable ItemStack ingredient();

    interface RecipeContexted extends CruxIngredientContext {
        @NotNull CruxRecipeContext recipeContext();
    }
}
