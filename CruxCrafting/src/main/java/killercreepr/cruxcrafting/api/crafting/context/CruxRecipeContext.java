package killercreepr.cruxcrafting.api.crafting.context;

import killercreepr.cruxcrafting.api.crafting.CruxCraftingMatrix;
import killercreepr.cruxcrafting.core.crafting.context.SimpleCraftingRecipeContext;
import org.jetbrains.annotations.NotNull;

public interface CruxRecipeContext {
    static CruxCraftingRecipeContext craftingRecipeContext(@NotNull CruxCraftingMatrix matrix){
        return new SimpleCraftingRecipeContext(matrix);
    }
}
