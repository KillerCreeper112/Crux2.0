package killercreepr.cruxcrafting.core.crafting.recipe;

import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.cruxcrafting.api.crafting.CruxCraftingMatrix;
import killercreepr.cruxcrafting.api.crafting.CruxRecipeResult;
import killercreepr.cruxcrafting.api.crafting.context.CruxCraftingRecipeContext;
import killercreepr.cruxcrafting.api.crafting.context.CruxIngredientContext;
import killercreepr.cruxcrafting.api.crafting.context.CruxRecipeContext;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxShapedRecipe;
import killercreepr.cruxcrafting.core.crafting.SimpleRecipeResult;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleShapedRecipe implements CruxShapedRecipe, Keyed {
    protected final Key key;
    protected final Map<Integer, CruxRecipeIngredient> ingredients;
    protected final List<DynamicItem> results;
    protected final int width;
    protected final int height;

    public SimpleShapedRecipe(Key key, Map<Integer, CruxRecipeIngredient> ingredients, List<DynamicItem> results, int width, int height) {
        this.key = key;
        this.ingredients = ingredients;
        this.results = results;
        this.width = width;
        this.height = height;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    public List<ItemStack> buildBaseResultItems(){
        List<ItemStack> list = new ArrayList<>();
        for(DynamicItem holder : results){
            list.add(holder.buildItem(TextParserContext.empty()));
        }
        return list;
    }

    public CruxRecipeResult craftResult(@NotNull CruxCraftingMatrix matrix, CruxCraftingRecipeContext ctx){
        List<ItemStack> resultItems = buildBaseResultItems();
        //todo ingredient onCraft
        for(Map.Entry<Integer, CruxRecipeIngredient> entry : mapIngredients(matrix).entrySet()){
            int slot = entry.getKey();
            CruxRecipeIngredient ingredient = entry.getValue();
            ItemStack itemIngredient = matrix.getItem(slot);
            //todo ingredient onCraft
            //result = ingredient.onCraft(result, itemIngredient);
        }
        return new SimpleRecipeResult(resultItems);
    }

    /**
     * Only used for showcasing what items this recipe will return.
     */
    @Override
    public @NotNull List<ItemStack> getDisplayedResultItems() {
        return buildBaseResultItems();
    }

    @Override
    public @Nullable CruxRecipeResult testResult(@NotNull CruxCraftingRecipeContext ctx) {
        var matrix = ctx.craftingMatrix();
        for(int i = 0; i <= matrix.getWidth() - this.width; ++i) {
            for(int j = 0; j <= matrix.getHeight() - this.height; ++j) {
                if (this.matches(matrix,  i, j, true, ctx)) {
                    return craftResult(matrix, ctx);
                }

                if (this.matches(matrix, i, j, false, ctx)) {
                    return craftResult(matrix, ctx);
                }
            }
        }
        return null;
    }

    @Override
    public @NotNull Map<Integer, CruxRecipeIngredient> mapIngredients(@NotNull CruxCraftingMatrix matrix){
        CruxRecipeContext ctx = CruxRecipeContext.craftingRecipeContext(matrix);
        for(int i = 0; i <= matrix.getWidth() - this.width; ++i) {
            for(int j = 0; j <= matrix.getHeight() - this.height; ++j) {

                Map<Integer, CruxRecipeIngredient> mapped = mapIngredients(matrix, i, j, true, ctx);
                if(mapped.size() == ingredients.size()) return mapped;

                mapped = mapIngredients(matrix, i, j, false, ctx);
                if(mapped.size() == ingredients.size()) return mapped;
            }
        }
        return Map.of();
    }

    @Override
    public @NotNull Map<Integer, CruxRecipeIngredient> mapIngredientDisplay() {
        return ingredients;
    }

    @Override
    public @NotNull Map<Integer, CruxRecipeIngredient> getIngredients() {
        return ingredients;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public @NotNull Map<Integer, CruxRecipeIngredient> mapIngredientsToMatrix(CruxCraftingMatrix matrix, int offsetX, int offsetY, boolean flipped){
        Map<Integer, CruxRecipeIngredient> map = new HashMap<>();
        int invWidth = matrix.getWidth();
        int invHeight = matrix.getHeight();
        for(int k = 0; k < invWidth; ++k) {
            for(int l = 0; l < invHeight; ++l) {
                int i1 = k - offsetX;
                int j1 = l - offsetY;
                CruxRecipeIngredient ingredient = CruxRecipeIngredient.EMPTY;
                if (i1 >= 0 && j1 >= 0 && i1 < this.width && j1 < this.height) {
                    if (flipped) {
                        ingredient = ingredients.get(this.width - i1 - 1 + j1 * this.width);
                    } else {
                        ingredient = ingredients.get(i1 + j1 * this.width);
                    }
                }
                if(ingredient == null) ingredient = CruxRecipeIngredient.EMPTY;
                int slot = k + l * invWidth;
                map.put(slot, ingredient);
            }
        }

        return map;
    }

    public Map<Integer, CruxRecipeIngredient> mapIngredients(CruxCraftingMatrix matrix, int offsetX, int offsetY, boolean flipped,
                                                             @NotNull CruxRecipeContext recipeCtx){
        Map<Integer, CruxRecipeIngredient> map = new HashMap<>();
        int invWidth = matrix.getWidth();
        int invHeight = matrix.getHeight();
        for(int k = 0; k < invWidth; ++k) {
            for(int l = 0; l < invHeight; ++l) {
                int i1 = k - offsetX;
                int j1 = l - offsetY;
                CruxRecipeIngredient ingredient = CruxRecipeIngredient.EMPTY;
                boolean contains = false;
                if (i1 >= 0 && j1 >= 0 && i1 < this.width && j1 < this.height) {
                    if (flipped) {
                        if(!ingredients.containsKey(this.width - i1 - 1 + j1 * this.width)) continue;
                        contains = true;
                        ingredient = ingredients.get(this.width - i1 - 1 + j1 * this.width);
                    } else {
                        if(!ingredients.containsKey(i1 + j1 * this.width)) continue;
                        contains = true;
                        ingredient = ingredients.get(i1 + j1 * this.width);
                    }
                }
                if(!contains) continue;
                if(ingredient == null) ingredient = CruxRecipeIngredient.EMPTY;
                int slot = k + l * invWidth;

                CruxIngredientContext ctx = CruxIngredientContext.ingredientContext(
                    matrix.getItem(slot), recipeCtx
                );
                if (!ingredient.test(ctx)) {
                    continue;
                }
                map.put(slot, ingredient);
            }
        }

        return map;
    }

    private boolean matches(CruxCraftingMatrix matrix, int offsetX, int offsetY, boolean flipped,
                            @NotNull CruxRecipeContext recipeCtx) {
        int invWidth = matrix.getWidth();
        int invHeight = matrix.getHeight();
        for(int k = 0; k < invWidth; ++k) {
            for(int l = 0; l < invHeight; ++l) {
                int i1 = k - offsetX;
                int j1 = l - offsetY;
                CruxRecipeIngredient ingredient = CruxRecipeIngredient.EMPTY;
                if (i1 >= 0 && j1 >= 0 && i1 < this.width && j1 < this.height) {
                    if (flipped) {
                        ingredient = ingredients.get(this.width - i1 - 1 + j1 * this.width);
                    } else {
                        ingredient = ingredients.get(i1 + j1 * this.width);
                    }
                }
                if(ingredient == null) ingredient = CruxRecipeIngredient.EMPTY;
                int slot = k + l * invWidth;
                CruxIngredientContext ctx = CruxIngredientContext.ingredientContext(
                    matrix.getItem(slot), recipeCtx
                );
                if (!ingredient.test(ctx)) {
                    return false;
                }
            }
        }

        return true;
    }
}
