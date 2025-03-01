package killercreepr.cruxenchants.api.crafting.context;

import killercreepr.cruxenchants.api.crafting.CruxCraftingMatrix;
import killercreepr.cruxenchants.core.crafting.context.SimpleCraftingRecipeContext;
import org.jetbrains.annotations.NotNull;

public interface CruxRecipeContext {
    static CruxCraftingRecipeContext craftingRecipeContext(@NotNull CruxCraftingMatrix matrix){
        return new SimpleCraftingRecipeContext(matrix);
    }
}
