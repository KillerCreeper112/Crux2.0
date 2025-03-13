package killercreepr.cruxcrafting.core.entity.memory;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.data.Loadable;
import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.entity.memory.EntityDataHolder;
import killercreepr.crux.core.util.CruxKey;
import killercreepr.cruxconfig.config.bukkit.file.BukkitDataFile;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.common.file.DataFile;
import killercreepr.cruxcrafting.api.crafting.CruxRecipeManager;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

public class RecipesHolder extends EntityDataHolder implements Loadable {
    protected final CruxRecipeManager<?> recipeManager;

    public RecipesHolder(@NotNull Key key, @NotNull EntityMemory parent, CruxRecipeManager<?> recipeManager) {
        super(key, parent);
        this.recipeManager = recipeManager;
        load();
    }

    /**
     * This is called immediately when the EntityMemory that this is attached to is supposed to be
     * removed from memory (e.x., when a player quits the server or when an entity is removed from world)
     *
     * @param e
     */
    @Override
    public void onMemoryUnload(@NotNull Entity e) {
        super.onMemoryUnload(e);
        save();
    }

    public CruxRecipeManager<?> getRecipeManager() {
        return recipeManager;
    }

    protected final Collection<Key> recipes = new HashSet<>();

    public boolean hasRecipe(Key key){
        return recipes.contains(key);
    }

    public boolean grantRecipes(Collection<Key> recipes){
        return this.recipes.addAll(recipes);
    }
    public boolean revokeRecipes(Collection<Key> recipes){
        return this.recipes.removeAll(recipes);
    }
    public boolean grantRecipe(Key recipes){
        return this.recipes.add(recipes);
    }
    public boolean revokeRecipe(Key recipes){
        return this.recipes.remove(recipes);
    }

    public Collection<Key> getRecipes() {
        return recipes;
    }

    public DataFile getDataFile(boolean createIfNeeded){
        return BukkitDataFile.parseFromGeneralPath(
            new CruxFolder(Crux.getMainPlugin(), "data/cruxcrafting/player/recipes/" +
                CruxKey.toFileName(((Keyed) recipeManager).key()) + "/" +
                parent.getUUID() + ".json").file(), createIfNeeded
        );
    }

    @Override
    public void save() {
        DataFile file = getDataFile(!recipes.isEmpty());
        if(file == null) return;
        file.serialize("recipes", recipes);
        file.save();
    }

    @Override
    public void load() {
        DataFile file = getDataFile(false);
        if(file==null) return;
        Collection<Key> parsed = file.deserialize("recipes", new TypeToken<Collection<Key>>(){}.getType());
        file.close();
        if(parsed != null) recipes.addAll(parsed);
    }
}
