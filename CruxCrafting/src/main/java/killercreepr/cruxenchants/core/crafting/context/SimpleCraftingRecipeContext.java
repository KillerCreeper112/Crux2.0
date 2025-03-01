package killercreepr.cruxenchants.core.crafting.context;

import killercreepr.cruxenchants.api.crafting.CruxCraftingMatrix;
import killercreepr.cruxenchants.api.crafting.context.CruxCraftingRecipeContext;
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
