package killercreepr.cruxcrafting.api.crafting;

import killercreepr.crux.core.Crux;
import killercreepr.cruxcrafting.core.crafting.SimpleRecipeCategory;
import killercreepr.cruxcrafting.core.registries.CruxCraftingRegistries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;

public interface RecipeCategory extends Keyed {
    static RecipeCategory craftingCategory(Key key){
        return new SimpleRecipeCategory(key);
    }
    static void register(){}

    RecipeCategory EQUIPMENT = register(craftingCategory(Crux.key("equipment")));
    RecipeCategory BUILDING = register(craftingCategory(Crux.key("building")));
    RecipeCategory MISC = register(craftingCategory(Crux.key("misc")));

    private static RecipeCategory register(RecipeCategory category){
        return CruxCraftingRegistries.RECIPE_CATEGORY.register(category);
    }
}
