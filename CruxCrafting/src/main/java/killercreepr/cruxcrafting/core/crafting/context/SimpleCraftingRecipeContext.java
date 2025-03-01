package killercreepr.cruxcrafting.core.crafting.context;

import killercreepr.cruxcrafting.api.crafting.CruxCraftingMatrix;
import killercreepr.cruxcrafting.api.crafting.context.CruxCraftingRecipeContext;
import org.jetbrains.annotations.NotNull;

public class SimpleCraftingRecipeContext implements CruxCraftingRecipeContext {
    protected final CruxCraftingMatrix craftingMatrix;

    public SimpleCraftingRecipeContext(CruxCraftingMatrix craftingMatrix) {
        this.craftingMatrix = craftingMatrix;
    }

    @Override
    public @NotNull CruxCraftingMatrix craftingMatrix() {
        return craftingMatrix;
    }
}
