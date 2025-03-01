package killercreepr.cruxenchants.core.config.handler;

import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxenchants.api.config.handler.FileCruxCraftingRecipe;
import killercreepr.cruxenchants.api.crafting.recipe.CruxCraftingRecipe;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileSimpleCruxCraftingRecipe extends SimpleFileHandler<CruxCraftingRecipe> implements FileCruxCraftingRecipe {
    protected final MappedRegistry<String, FileCruxCraftingRecipe> craftingHandlers =
        MappedRegistry.mappedRegistry();

    public void registerHandler(String id, FileCruxCraftingRecipe handler){
        craftingHandlers.register(id, handler);
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull CruxCraftingRecipe object) {
        throw new UnsupportedOperationException("NO SIR");
    }

    @Override
    public @Nullable CruxCraftingRecipe deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        Key key = ctx.getRegistry().deserializeFromFile(Key.class, o.get("key"));
        if(key==null) return null;
        return deserializeFromFile(ctx, e, key);
    }

    public @Nullable CruxCraftingRecipe deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e,
                                                            @NotNull Key key) {
        if(!(e instanceof FileObject o)) return null;

        String type = o.getObject(String.class, "recipe_type");
        if(type == null) return null;
        var handler = craftingHandlers.get(type);
        if(handler == null) throw new IllegalArgumentException("Recipe type of " + type + " does not exist!");
        return handler.deserializeFromFile(ctx, e, key);
    }
}
