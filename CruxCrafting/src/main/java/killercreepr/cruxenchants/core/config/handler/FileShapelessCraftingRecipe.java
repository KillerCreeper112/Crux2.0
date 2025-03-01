package killercreepr.cruxenchants.core.config.handler;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxenchants.api.config.handler.FileCruxCraftingRecipe;
import killercreepr.cruxenchants.api.crafting.ingredient.CruxRecipeIngredient;
import killercreepr.cruxenchants.api.crafting.recipe.CruxCraftingRecipe;
import killercreepr.cruxenchants.core.crafting.recipe.SimpleShapelessRecipe;
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
        return new SimpleShapelessRecipe(key, ingredients, results);
    }
}
