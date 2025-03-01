package killercreepr.cruxenchants.core.crafting.context;

import killercreepr.cruxenchants.api.crafting.context.CruxIngredientContext;
import killercreepr.cruxenchants.api.crafting.context.CruxRecipeContext;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleIngredientContext implements CruxIngredientContext {
    protected final ItemStack ingredient;

    public SimpleIngredientContext(ItemStack ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public @Nullable ItemStack ingredient() {
        return ingredient;
    }

    public static class RecipeContexted extends SimpleIngredientContext implements CruxIngredientContext.RecipeContexted{
        protected final CruxRecipeContext recipeContext;
        public RecipeContexted(ItemStack ingredient, CruxRecipeContext recipeContext) {
            super(ingredient);
            this.recipeContext = recipeContext;
        }
        @Override
        public @NotNull CruxRecipeContext recipeContext() {
            return recipeContext;
        }
    }
}
