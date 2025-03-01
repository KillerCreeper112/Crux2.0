package killercreepr.cruxcrafting.core.crafting;

import killercreepr.cruxcrafting.api.crafting.CruxRecipeManager;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxRecipe;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SimpleRecipeManager<T extends CruxRecipe> implements CruxRecipeManager<T> {
    protected final Map<Key, T> recipes = new HashMap<>();

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

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return recipes.values().iterator();
    }
}
