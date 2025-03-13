package killercreepr.cruxcrafting.core.menu.module;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.cruxcrafting.api.crafting.CruxCraftingRecipeManager;
import killercreepr.cruxcrafting.api.crafting.RecipeCategory;
import killercreepr.cruxcrafting.api.crafting.recipe.CategorizedRecipe;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxCraftingRecipe;
import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.api.menu.module.ActiveMenuModule;
import killercreepr.cruxmenus.core.menu.module.standard.PagedMenuModule;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
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

    public CruxCraftingRecipeManager getRecipeManager() {
        return recipeManager;
    }

    @Override
    public @Nullable ActiveMenuModule build(@NotNull Menu menu) {
        return new ActivePagedCruxCraftingRecipeMenuModule(
            id, this, indexes, null, getValues(menu)
        );
    }

    public Key getSelectedRecipeCategory(Menu menu){
        if(!(menu instanceof CfgMenu cfg)) return null;
        DataExchange info = cfg.info();
        Object object = info.get("selected_recipe_category");
        if(object == null) return null;
        if(object instanceof Key k) return k;
        if(object instanceof RecipeCategory c) return c.key();
        return Crux.key(object.toString());
    }

    @Override
    public @NotNull Holder<List<CruxCraftingRecipe>> getValues(@NotNull Menu menu) {
        return () ->{
            if(recipeManager.getRecipes() instanceof List<CruxCraftingRecipe> l) return l;
            Key selectedCategory = getSelectedRecipeCategory(menu);

            var list = new ArrayList<>(recipeManager.getRecipes());
            if(selectedCategory != null) list.removeIf(d ->{
                if(!(d instanceof CategorizedRecipe e)) return true;
                return !e.getRecipeCategory().key().equals(selectedCategory);
            });
            list.sort(Comparator.comparing(recipe ->{
                if(recipe instanceof Keyed k) return k.key().value();
                return "a";
            }));
            return list;
        };
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
