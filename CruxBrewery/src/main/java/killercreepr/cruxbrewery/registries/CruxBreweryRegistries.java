package killercreepr.cruxbrewery.registries;

import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.core.registry.SimpleKeyedRegistry;
import killercreepr.cruxbrewery.recipe.BrewRecipe;
import org.bukkit.inventory.BrewerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxBreweryRegistries {
    public static final KeyedRegistry<BrewRecipe> BREW_RECIPES = new SimpleKeyedRegistry<>();
    public static @Nullable BrewRecipe getBrewRecipe(@NotNull BrewerInventory inv){
        for(BrewRecipe r : BREW_RECIPES){
            if(r.canBrew(inv)) return r;
        }
        return null;
    }
}
