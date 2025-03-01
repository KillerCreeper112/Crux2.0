package killercreepr.cruxenchants.core.config.handler;

import killercreepr.crux.api.item.predicate.ItemPredicate;
import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxenchants.api.crafting.ingredient.CruxRecipeIngredient;
import killercreepr.cruxenchants.core.crafting.ingredient.SimpleRecipeIngredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileCruxRecipeIngredient extends SimpleFileHandler<CruxRecipeIngredient> {
    protected final MappedRegistry<String, FileObjectHandler<? extends CruxRecipeIngredient>> recipeHandlers =
        MappedRegistry.mappedRegistry();

    public void registerHandler(String id, FileObjectHandler<? extends CruxRecipeIngredient> handler){
        recipeHandlers.register(id, handler);
    }
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull CruxRecipeIngredient object) {
        throw new UnsupportedOperationException("NOOOO SIIR");
    }

    @Override
    public @Nullable CruxRecipeIngredient deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)){
            ItemPredicate predicate = ctx.getRegistry().deserializeFromFile(ItemPredicate.class, e);
            if(predicate==null) return null;
            return new SimpleRecipeIngredient(predicate, 1);
        }
        String type = o.getObject(String.class, "ingredient");
        if(type == null){
            ItemPredicate itemPredicate = ctx.getRegistry().deserializeFromFile(ItemPredicate.class, o.get("item"));
            if(itemPredicate==null) return null;
            int amount = o.getObject(Integer.class, "amount", 1);
            return new SimpleRecipeIngredient(itemPredicate, amount);
        }
        var handler = recipeHandlers.get(type);
        if(handler == null){
            throw new IllegalArgumentException("Recipe ingredient type of " + type + " does not exist!");
        }
        return handler.deserializeFromFile(ctx, e);
    }
}
