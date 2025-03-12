package killercreepr.cruxcrafting.api.crafting;

import killercreepr.cruxcrafting.api.crafting.crafter.CruxCrafter;
import killercreepr.cruxcrafting.api.crafting.crafter.CruxCraftingCrafter;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxCraftingRecipe;
import killercreepr.cruxcrafting.core.menu.CraftingRecipeMenuViewer;
import org.jetbrains.annotations.NotNull;

public interface CrafterHolder {
    @NotNull CruxCrafter getCrafter();

    interface Crafting extends CrafterHolder{
        @Override
        @NotNull CruxCraftingCrafter getCrafter();
        @NotNull CraftingRecipeMenuViewer buildRecipeViewer(CruxCraftingRecipe recipe);
    }
}
