package killercreepr.cruxenchants.core.crafting.context;

import killercreepr.cruxenchants.api.crafting.context.CruxIngredientContext;
import killercreepr.cruxenchants.api.crafting.context.CruxRecipeContext;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleIngredientContext implements CruxIngredientContext {
    protected final ItemStack ingredient;
    protected final CruxRecipeContext recipeContext;

    public SimpleIngredientContext(ItemStack ingredient, CruxRecipeContext recipeContext) {
        this.ingredient = ingredient;
        this.recipeContext = recipeContext;
    }

    @Override
    public @Nullable ItemStack ingredient() {
        return ingredient;
    }

    @Override
    public @NotNull CruxRecipeContext recipeContext() {
        return recipeContext;
    }
}
