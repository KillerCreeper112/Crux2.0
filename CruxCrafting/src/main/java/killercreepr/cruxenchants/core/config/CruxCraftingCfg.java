package killercreepr.cruxenchants.core.config;

import killercreepr.cruxenchants.core.config.handler.FileShapedCraftingRecipe;
import killercreepr.cruxenchants.core.config.handler.FileShapelessCraftingRecipe;
import killercreepr.cruxenchants.core.config.handler.FileSimpleCruxCraftingRecipe;

public class CruxCraftingCfg {
    public static final FileSimpleCruxCraftingRecipe FILE_CRUX_CRAFTING_RECIPE = new FileSimpleCruxCraftingRecipe();

    public static void onLoad(){
        registerRecipes(FILE_CRUX_CRAFTING_RECIPE);
    }

    public static void registerRecipes(FileSimpleCruxCraftingRecipe file){
        file.registerHandler("shaped", new FileShapedCraftingRecipe());
        file.registerHandler("shapeless", new FileShapelessCraftingRecipe());
    }
}
