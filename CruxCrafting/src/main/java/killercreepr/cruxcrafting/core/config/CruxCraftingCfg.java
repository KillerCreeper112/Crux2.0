package killercreepr.cruxcrafting.core.config;

import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import killercreepr.cruxcrafting.core.config.handler.*;

public class CruxCraftingCfg {
    public static final FileSimpleCruxCraftingRecipe FILE_CRUX_CRAFTING_RECIPE = new FileSimpleCruxCraftingRecipe();
    public static final SimpleFileCruxRecipeIngredient FILE_CRUX_RECIPE_INGREDIENT = new SimpleFileCruxRecipeIngredient();

    public static void onLoad(){
        registerRecipes(FILE_CRUX_CRAFTING_RECIPE);
        registerRecipeIngredients(FILE_CRUX_RECIPE_INGREDIENT);

        CfgRegistries.SIMPLE_REGISTRY.forEach(CruxCraftingCfg::registerFileHandler);
    }

    public static void registerFileHandler(FileRegistry reg){
        reg.registerFileHandler(CruxRecipeIngredient.class, FILE_CRUX_RECIPE_INGREDIENT);
    }

    public static void registerRecipes(FileSimpleCruxCraftingRecipe file){
        file.registerHandler("shaped", new FileShapedCraftingRecipe());
        file.registerHandler("shapeless", new FileShapelessCraftingRecipe());
    }

    public static void registerRecipeIngredients(SimpleFileCruxRecipeIngredient file){
        file.registerHandler("any_of", new FileAnyOfIngredient());
    }
}
