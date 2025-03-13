package killercreepr.cruxcrafting.core.config.handler;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxcrafting.api.config.handler.FileCruxCraftingRecipe;
import killercreepr.cruxcrafting.api.crafting.CruxCraftingRecipeManager;
import killercreepr.cruxcrafting.api.crafting.RecipeCategory;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxCraftingRecipe;
import killercreepr.cruxcrafting.core.crafting.recipe.SimpleShapedCategorizedRecipe;
import killercreepr.cruxcrafting.core.crafting.recipe.SimpleShapedRecipe;
import killercreepr.cruxcrafting.core.registries.CruxCraftingRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FileShapedCraftingRecipe implements FileCruxCraftingRecipe {
    public @Nullable CruxCraftingRecipe deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e,
                                                            @NotNull Key key) {
        if(!(e instanceof FileObject o)) return null;

        FileRegistry reg = ctx.getRegistry();
        List<DynamicItem> results = reg.deserializeFromFile(
            new TypeToken<List<DynamicItem>>(){}.getType(),
            o.get("results")
        );
        if(results == null || results.isEmpty()) return null;

        List<String> shapeList = reg.deserializeFromFile(
            new TypeToken<List<String>>(){}.getType(),
            o.get("shape")
        );
        if(shapeList==null) return null;
        List<String> shape = new ArrayList<>();
        int width = 0;
        for(String s : shapeList){
            String[] args = s.split("");
            shape.addAll(Arrays.asList(args));
            if(args.length <= width) continue;
            width = args.length;
        }
        int length = shapeList.size();

        Map<Integer, Character> shapeMap = new HashMap<>();
        int index = -1;
        for(String s : shape){
            index++;
            if(s.isBlank()) continue;
            shapeMap.put(index, s.charAt(0));
        }

        Map<String, CruxRecipeIngredient> map = reg.deserializeFromFile(
            new TypeToken<Map<String, CruxRecipeIngredient>>(){}.getType(),
            o.get("ingredients")
        );
        if(map==null) return null;

        Map<Character, CruxRecipeIngredient> charToIngredient = new HashMap<>();
        Map<Integer, CruxRecipeIngredient> ingredients = new HashMap<>();

        map.forEach((s, ing) -> charToIngredient.put(s.charAt(0), ing));

        shapeMap.forEach((slot, character) ->{
            CruxRecipeIngredient ingredient = charToIngredient.get(character);
            if(ingredient == null) return;
            ingredients.put(slot, ingredient);
        });
        Collection<Key> category = reg.deserializeFromFile(new TypeToken<Collection<Key>>(){}.getType(), o.get("categories"));
        if(category != null){
            Collection<RecipeCategory> recipeCategory = new HashSet<>();
            for(Key k : category){
                var got = CruxCraftingRegistries.RECIPE_CATEGORY.get(k);
                if(got == null){
                    Crux.logWarning("RecipeCategory of " + category + " not found! FileShapedCraftingRecipe" + e);
                }else recipeCategory.add(got);
            }
            return new SimpleShapedCategorizedRecipe(key, ingredients, results, width, length, recipeCategory);
        }

        return new SimpleShapedRecipe(key, ingredients, results, width, length);
    }
}
