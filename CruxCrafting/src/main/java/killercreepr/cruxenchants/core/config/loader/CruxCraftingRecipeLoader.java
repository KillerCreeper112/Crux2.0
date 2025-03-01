package killercreepr.cruxenchants.core.config.loader;

import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.bukkit.loader.CfgLoader;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxenchants.api.config.handler.FileCruxCraftingRecipe;
import killercreepr.cruxenchants.api.crafting.recipe.CruxCraftingRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CruxCraftingRecipeLoader extends CfgLoader {
    protected final FileCruxCraftingRecipe file;
    protected final Consumer<CruxCraftingRecipe> onCreated;

    public CruxCraftingRecipeLoader(FileCruxCraftingRecipe file, Consumer<CruxCraftingRecipe> onCreated) {
        this.file = file;
        this.onCreated = onCreated;
    }

    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path){
        CruxCraftingRecipe table;
        if(path == null){
            if(!(cfg.getRoot() instanceof FileObject root)) return;
            if(file instanceof FileObjectHandler<?> h){
                table = (CruxCraftingRecipe) h.deserializeFromFile(new FileContext<>(cfg.fileRegistry()), root);
            }else table = null;
        }
        else{
            if(!(cfg.getRoot() instanceof FileObject root)) return;

            table = file.deserializeFromFile(
                new FileContext<>(cfg.fileRegistry()), root, Crux.key(path)
            );
        }
        if(table == null) return;
        onCreated.accept(table);
    }
}
