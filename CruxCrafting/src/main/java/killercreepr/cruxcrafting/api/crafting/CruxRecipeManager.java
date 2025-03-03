package killercreepr.cruxcrafting.api.crafting;

import killercreepr.cruxcrafting.api.crafting.recipe.CruxRecipe;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface CruxRecipeManager<T extends CruxRecipe> extends Iterable<T> {
    /*@NotNull
    Map<Key, CruxRecipe> getRecipes();*/
    @Nullable T getRecipe(Key key);
    void addRecipe(@NotNull T recipe);
    @Nullable T removeRecipe(Key key);
    boolean hasRecipe(@NotNull Key key);
    @NotNull Collection<T> getRecipes();
}
