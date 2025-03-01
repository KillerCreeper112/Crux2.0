package killercreepr.cruxcrafting.core.config.handler;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxcrafting.api.config.handler.FileCruxRecipeIngredient;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import killercreepr.cruxcrafting.core.crafting.ingredient.AnyRecipeIngredient;
import killercreepr.cruxcrafting.core.crafting.ingredient.KeyedAnyRecipeIngredient;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileAnyOfIngredient implements FileCruxRecipeIngredient {

    @Override
    public @Nullable CruxRecipeIngredient deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @Nullable Key key) {
        if(!(e instanceof FileObject o)) return null;

        Collection<CruxRecipeIngredient> ingredients = ctx.getRegistry().deserializeFromFile(
            new TypeToken<Collection<CruxRecipeIngredient>>(){}.getType(), o.get("ingredients")
        );

        if(ingredients==null || ingredients.isEmpty()) return null;

        List<DynamicItem> displays = ctx.getRegistry().deserializeFromFile(
            new TypeToken<List<DynamicItem>>(){}.getType(), o.get("displays")
        );
        List<ItemStack> parsed;
        if(displays != null && !displays.isEmpty()){
            parsed = new ArrayList<>();
            for(DynamicItem i : displays){
                ItemStack built = i.buildItem(TextParserContext.empty());
                if(built==null) continue;
                parsed.add(built);
            }
        }else parsed = null;

        if(key == null) return new AnyRecipeIngredient(ingredients, parsed);
        return new KeyedAnyRecipeIngredient(ingredients, parsed, key);
    }
}
