package killercreepr.cruxcrafting.core.config.handler;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxcrafting.api.config.handler.FileCruxCraftingRecipe;
import killercreepr.cruxcrafting.api.crafting.RecipeCategory;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxCraftingRecipe;
import killercreepr.cruxcrafting.core.crafting.recipe.SimpleShapedCategorizedRecipe;
import killercreepr.cruxcrafting.core.crafting.recipe.SimpleShapelessCategorizedRecipe;
import killercreepr.cruxcrafting.core.crafting.recipe.SimpleShapelessRecipe;
import killercreepr.cruxcrafting.core.registries.CruxCraftingRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class FileShapelessCraftingRecipe implements FileCruxCraftingRecipe {
    public @Nullable CruxCraftingRecipe deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e,
                                                            @NotNull Key key) {
        if(!(e instanceof FileObject o)) return null;

        FileRegistry reg = ctx.getRegistry();
        List<DynamicItem> results = reg.deserializeFromFile(
            new TypeToken<List<DynamicItem>>(){}.getType(),
            o.get("results")
        );
        if(results == null || results.isEmpty()) return null;

        Collection<CruxRecipeIngredient> ingredients = reg.deserializeFromFile(
            new TypeToken<Collection<CruxRecipeIngredient>>(){}.getType(),
            o.get("ingredients")
        );
        if(ingredients==null) return null;

        Key category = reg.deserializeFromFile(Key.class, o.get("category"));
        if(category != null){
            RecipeCategory recipeCategory = CruxCraftingRegistries.RECIPE_CATEGORY.get(category);
            if(recipeCategory == null){
                Crux.logWarning("RecipeCategory of " + category + " not found! FileShapedCraftingRecipe" + e);
            }else{
                new SimpleShapelessCategorizedRecipe(key, ingredients, results, recipeCategory);
            }
        }

        return new SimpleShapelessRecipe(key, ingredients, results);
    }
}
