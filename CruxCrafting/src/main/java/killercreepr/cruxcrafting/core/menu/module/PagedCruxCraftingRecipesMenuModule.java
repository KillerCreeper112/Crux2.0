package killercreepr.cruxcrafting.core.menu.module;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxcrafting.api.crafting.CruxCraftingRecipeManager;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxCraftingRecipe;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.api.menu.module.ActiveMenuModule;
import killercreepr.cruxmenus.core.menu.module.standard.PagedMenuModule;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PagedCruxCraftingRecipesMenuModule extends PagedMenuModule<CruxCraftingRecipe> {
    protected final Key key;
    protected final CruxCraftingRecipeManager recipeManager;
    public PagedCruxCraftingRecipesMenuModule(@NotNull String id, @NotNull NumberProvider indexes,
                                              @Nullable String valueFilter,
                                              @Nullable MenuItems valueItems,
                                              @Nullable MenuItems emptyItems, Key key, CruxCraftingRecipeManager recipeManager) {
        super(id, indexes, valueFilter, valueItems, emptyItems);
        this.key = key;
        this.recipeManager = recipeManager;
    }

    @Override
    public @Nullable ActiveMenuModule build(@NotNull Menu menu) {
        return new ActivePagedCruxCraftingRecipeMenuModule(
            id, this, indexes, null, getValues(menu)
        );
    }

    @Override
    public @NotNull Holder<List<CruxCraftingRecipe>> getValues(@NotNull Menu menu) {
        return () ->{
            if(recipeManager.getRecipes() instanceof List<CruxCraftingRecipe> l) return l;
            return new ArrayList<>(recipeManager.getRecipes());
        };
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
