package killercreepr.cruxenchants.api.crafting.context;

import killercreepr.cruxenchants.api.crafting.CruxCraftingMatrix;
import org.jetbrains.annotations.NotNull;

public interface CruxCraftingRecipeContext extends CruxRecipeContext {
    @NotNull CruxCraftingMatrix craftingMatrix();
}
