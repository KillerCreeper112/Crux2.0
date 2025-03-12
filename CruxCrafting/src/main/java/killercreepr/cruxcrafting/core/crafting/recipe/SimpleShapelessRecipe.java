package killercreepr.cruxcrafting.core.crafting.recipe;

import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.cruxcrafting.api.crafting.CruxCraftingMatrix;
import killercreepr.cruxcrafting.api.crafting.CruxRecipeResult;
import killercreepr.cruxcrafting.api.crafting.context.CruxCraftingRecipeContext;
import killercreepr.cruxcrafting.api.crafting.context.CruxIngredientContext;
import killercreepr.cruxcrafting.api.crafting.context.CruxRecipeContext;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxShapelessRecipe;
import killercreepr.cruxcrafting.core.crafting.SimpleRecipeResult;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SimpleShapelessRecipe implements CruxShapelessRecipe, Keyed {
    protected final Key key;
    protected final Collection<CruxRecipeIngredient> ingredients;
    protected final List<DynamicItem> results;

    public SimpleShapelessRecipe(Key key, Collection<CruxRecipeIngredient> ingredients, List<DynamicItem> results) {
        this.key = key;
        this.ingredients = ingredients;
        this.results = results;
    }

    @Override
    public @NotNull Collection<CruxRecipeIngredient> getIngredients() {
        return ingredients;
    }

    public @NotNull Map<Integer, CruxRecipeIngredient> mapIngredients(@NotNull CruxCraftingMatrix matrix,
                                                                      @NotNull CruxRecipeContext recipeCtx) {
        Collection<CruxRecipeIngredient> copy = new ArrayList<>(ingredients);
        Map<Integer, CruxRecipeIngredient> map = new HashMap<>();
        int invWidth = matrix.getWidth();
        int invHeight = matrix.getHeight();
        for(int k = 0; k < invWidth; ++k) {
            for(int l = 0; l < invHeight; ++l) {
                int slot = k + l * invWidth;
                CruxIngredientContext ctx = CruxIngredientContext.ingredientContext(
                    matrix.getItem(slot), recipeCtx
                );
                CruxRecipeIngredient ingredient = null;
                for(CruxRecipeIngredient in : copy){
                    if(!in.test(ctx)) continue;
                    copy.remove(in);
                    ingredient = in;
                    break;
                }
                if(ingredient==null) continue;

                map.put(slot, ingredient);
            }
        }

        return map;
    }

    @Override
    public @NotNull Map<Integer, CruxRecipeIngredient> mapIngredients(@NotNull CruxCraftingMatrix matrix) {
        return mapIngredients(matrix, CruxRecipeContext.craftingRecipeContext(matrix));
    }

    @Override
    public @NotNull Map<Integer, CruxRecipeIngredient> mapIngredientDisplay() {
        Map<Integer, CruxRecipeIngredient> map = new HashMap<>();
        int index = 0;
        for(CruxRecipeIngredient in : ingredients){
            map.put(index, in);
            index++;
        }
        return map;
    }

    @Override
    public @NotNull Map<Integer, CruxRecipeIngredient> mapIngredientDisplay(int width, int height) {
        return mapIngredientDisplay();
    }

    /**
     * Only used for showcasing what items this recipe will return.
     */
    @Override
    public @NotNull List<ItemStack> getDisplayedResultItems() {
        return buildBaseResultItems();
    }

    public List<ItemStack> buildBaseResultItems(){
        List<ItemStack> list = new ArrayList<>();
        for(DynamicItem holder : results){
            list.add(holder.buildItem(TextParserContext.empty()));
        }
        return list;
    }

    @Override
    public @Nullable CruxRecipeResult testResult(@NotNull CruxCraftingRecipeContext ctx) {
        var matrix = ctx.craftingMatrix();
        if(matrix.getIngredientCount() != ingredients.size()) return null;
        Collection<CruxRecipeIngredient> copy = new ArrayList<>(ingredients);
        for(ItemStack item : matrix){
            CruxIngredientContext inCtx = CruxIngredientContext.ingredientContext(item, ctx);
            for(CruxRecipeIngredient in : copy){
                if(!in.test(inCtx)) continue;
                copy.remove(in);
                break;
            }
        }
        if(!copy.isEmpty()) return null;
        return new SimpleRecipeResult(buildBaseResultItems());
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
