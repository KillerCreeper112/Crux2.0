package killercreepr.cruxcrafting.core.crafting;

import killercreepr.cruxcrafting.api.crafting.CruxCraftingRecipeManager;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxCraftingRecipe;
import net.kyori.adventure.key.Key;

public class SimpleCraftingRecipeManager extends SimpleRecipeManager<CruxCraftingRecipe> implements CruxCraftingRecipeManager {

    public SimpleCraftingRecipeManager(Key key) {
        super(key);
    }
}
