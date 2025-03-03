package killercreepr.cruxcrafting.core.menu;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxCraftingRecipe;
import killercreepr.cruxmenus.api.menu.holder.MenuHolder;
import killercreepr.cruxmenus.core.menu.ConfigMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GenericRecipeViewMenu extends ConfigMenu {
    public GenericRecipeViewMenu(@NotNull MenuHolder holder, @NotNull DataExchange info) {
        super(holder, info);
    }

    public GenericRecipeViewMenu(@NotNull MenuHolder holder, @NotNull DataExchange info, @Nullable MergedTagContainer tags) {
        super(holder, info, tags);
    }

    public CruxCraftingRecipe getRecipe(){
        return info.getOrThrow("crafting_recipe", CruxCraftingRecipe.class);
    }

    protected CraftingRecipeMenuViewer recipeViewer;
    @Override
    public void onRefresh() {
        super.onRefresh();
        recipeViewer = new CraftingRecipeMenuViewer(inventory, getRecipe());
        recipeViewer.display();
    }
}
