package killercreepr.cruxenchants.core.config;

import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxenchants.api.crafting.ingredient.CruxRecipeIngredient;
import killercreepr.cruxenchants.core.config.handler.FileCruxRecipeIngredient;
import killercreepr.cruxenchants.core.config.handler.FileShapedCraftingRecipe;
import killercreepr.cruxenchants.core.config.handler.FileShapelessCraftingRecipe;
import killercreepr.cruxenchants.core.config.handler.FileSimpleCruxCraftingRecipe;

public class CruxCraftingCfg {
    public static final FileSimpleCruxCraftingRecipe FILE_CRUX_CRAFTING_RECIPE = new FileSimpleCruxCraftingRecipe();
    public static final FileCruxRecipeIngredient FILE_CRUX_RECIPE_INGREDIENT = new FileCruxRecipeIngredient();

    public static void onLoad(){
        registerRecipes(FILE_CRUX_CRAFTING_RECIPE);

        CfgRegistries.SIMPLE_REGISTRY.forEach(CruxCraftingCfg::registerFileHandler);
    }

    public static void registerFileHandler(FileRegistry reg){
        reg.registerFileHandler(CruxRecipeIngredient.class, FILE_CRUX_RECIPE_INGREDIENT);
    }

    public static void registerRecipes(FileSimpleCruxCraftingRecipe file){
        file.registerHandler("shaped", new FileShapedCraftingRecipe());
        file.registerHandler("shapeless", new FileShapelessCraftingRecipe());
    }
}
