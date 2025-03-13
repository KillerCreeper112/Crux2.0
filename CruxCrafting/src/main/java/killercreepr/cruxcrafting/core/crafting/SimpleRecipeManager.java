package killercreepr.cruxcrafting.core.crafting;

import killercreepr.cruxcrafting.api.crafting.CruxRecipeManager;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxRecipe;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SimpleRecipeManager<T extends CruxRecipe> implements CruxRecipeManager<T>, Keyed {
    protected final Map<Key, T> recipes = new HashMap<>();
    protected final Key key;

    public SimpleRecipeManager(Key key) {
        this.key = key;
    }

    @Override
    public @Nullable T getRecipe(Key key) {
        return recipes.get(key);
    }

    @Override
    public void addRecipe(@NotNull T recipe) {
        if(!(recipe instanceof Keyed keyed)) throw new IllegalArgumentException("Recipe must be keyed! " + recipe);
        recipes.put(keyed.key(), recipe);
    }

    @Override
    public T removeRecipe(Key key) {
        return recipes.remove(key);
    }

    @Override
    public boolean hasRecipe(@NotNull Key key) {
        return recipes.containsKey(key);
    }

    @Override
    public @NotNull Collection<T> getRecipes() {
        return recipes.values();
    }

    @Override
    public boolean hasRecipe(@NotNull Entity entity, @NotNull T recipe) {
        return true;
    }

    @Override
    public boolean grantRecipe(@NotNull Entity entity, @NotNull T recipe) {
        return false;
    }

    @Override
    public boolean revokeRecipe(@NotNull Entity entity, @NotNull T recipe) {
        return false;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return recipes.values().iterator();
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
