package killercreepr.cruxcrafting.core.config.loader;

import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.bukkit.loader.CfgLoader;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxcrafting.api.config.handler.FileCruxRecipeIngredient;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CruxCraftingIngredientLoader extends CfgLoader {
    protected final FileCruxRecipeIngredient file;
    protected final Consumer<CruxRecipeIngredient> onCreated;

    public CruxCraftingIngredientLoader(FileCruxRecipeIngredient file, Consumer<CruxRecipeIngredient> onCreated) {
        this.file = file;
        this.onCreated = onCreated;
    }

    public CruxCraftingIngredientLoader loadFromSingleFile(@NotNull DataFile cfg){
        if(!(cfg.getRoot() instanceof FileObject root)) return this;
        var ctx = new FileContext<>(cfg.fileRegistry());
        root.forEach((key, ele) ->{
            Key k = Crux.key(key);
            CruxRecipeIngredient ingredient = file.deserializeFromFile(ctx, ele, k);
            if(ingredient==null) return;
            onCreated.accept(ingredient);
        });
        return this;
    }

    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path){
        CruxRecipeIngredient table;
        if(path == null){
            if(!(cfg.getRoot() instanceof FileObject root)) return;
            if(file instanceof FileObjectHandler<?> h){
                table = (CruxRecipeIngredient) h.deserializeFromFile(new FileContext<>(cfg.fileRegistry()), root);
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
