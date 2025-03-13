package killercreepr.cruxcrafting.api.crafting;

import killercreepr.cruxcrafting.api.crafting.recipe.CruxRecipe;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
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

    /**
     * @return True if the entity has access to the recipe.
     */
    boolean hasRecipe(@NotNull Entity entity, @NotNull T recipe);
    /**
     * Grants the recipe to the entity.
     * @return True if the entity's recipe status has been changed.
     */
    boolean grantRecipe(@NotNull Entity entity, @NotNull T recipe);
    /**
     * Revokes the recipe from the entity.
     * @return True if the entity's recipe status has been changed.
     */
    boolean revokeRecipe(@NotNull Entity entity, @NotNull T recipe);
}
