package killercreepr.cruxcrafting.core.commands.arg;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import killercreepr.cruxcrafting.api.crafting.CruxRecipeManager;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxRecipe;

public interface CruxRecipeResolver {
    CruxRecipe resolve(CruxRecipeManager<?> recipeManager) throws CommandSyntaxException;
}
