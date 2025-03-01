package killercreepr.cruxcrafting.core.config.handler;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.item.predicate.ItemPredicate;
import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxcrafting.api.config.handler.FileCruxRecipeIngredient;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import killercreepr.cruxcrafting.core.crafting.ingredient.SimpleKeyedRecipeIngredient;
import killercreepr.cruxcrafting.core.crafting.ingredient.SimpleRecipeIngredient;
import killercreepr.cruxcrafting.core.crafting.ingredient.SimpleWrappedKeyedRecipeIngredient;
import killercreepr.cruxcrafting.core.crafting.ingredient.SimpleWrappedRecipeIngredient;
import killercreepr.cruxcrafting.core.registries.CruxCraftingRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SimpleFileCruxRecipeIngredient extends SimpleFileHandler<CruxRecipeIngredient> implements FileCruxRecipeIngredient {
    protected final MappedRegistry<String, FileCruxRecipeIngredient> recipeHandlers =
        MappedRegistry.mappedRegistry();

    public void registerHandler(String id, FileCruxRecipeIngredient handler){
        recipeHandlers.register(id, handler);
    }
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull CruxRecipeIngredient object) {
        throw new UnsupportedOperationException("NOOOO SIIR");
    }

    @Override
    public @Nullable CruxRecipeIngredient deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        return deserializeFromFile(ctx, e, null);
    }

    public @Nullable CruxRecipeIngredient deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e,
                                                              @Nullable Key key) {
        if(!(e instanceof FileObject o)){
            String id = e.getAsString();
            if(id.startsWith("#")){
                CruxRecipeIngredient ingredient = CruxCraftingRegistries.RECIPE_INGREDIENT.get(Crux.key(id.substring(1)));
                if(ingredient != null) return ingredient;
            }

            ItemPredicate predicate = ctx.getRegistry().deserializeFromFile(ItemPredicate.class, e);
            if(predicate==null) return null;
            return new SimpleRecipeIngredient(predicate, 1, null);
        }
        if(key == null) key = ctx.getRegistry().deserializeFromFile(Key.class, o.get("key"));
        String type = o.getObject(String.class, "ingredient_type");
        if(type == null){
            CruxRecipeIngredient base = ctx.getRegistry().deserializeFromFile(CruxRecipeIngredient.class, o.get("base"));

            int amount = o.getObject(Integer.class, "amount", 1);
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

            if(base != null){
                if(key != null) return new SimpleWrappedKeyedRecipeIngredient(base, amount, key);
                return new SimpleWrappedRecipeIngredient(base, amount);
            }
            ItemPredicate itemPredicate = ctx.getRegistry().deserializeFromFile(ItemPredicate.class, o.get("item"));
            if(itemPredicate==null) return null;

            if(key != null) return new SimpleKeyedRecipeIngredient(itemPredicate, amount, parsed, key);
            return new SimpleRecipeIngredient(itemPredicate, amount, parsed);
        }
        var handler = recipeHandlers.get(type);
        if(handler == null){
            throw new IllegalArgumentException("Recipe ingredient type of " + type + " does not exist!");
        }
        return handler.deserializeFromFile(ctx, e, key);
    }
}
