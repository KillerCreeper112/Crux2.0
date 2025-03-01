package killercreepr.cruxcrafting.api.crafting.context;

import killercreepr.cruxcrafting.api.crafting.CruxCraftingMatrix;
import org.jetbrains.annotations.NotNull;

public interface CruxCraftingRecipeContext extends CruxRecipeContext {
    @NotNull CruxCraftingMatrix craftingMatrix();
}
