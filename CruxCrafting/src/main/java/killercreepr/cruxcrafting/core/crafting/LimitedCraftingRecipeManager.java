package killercreepr.cruxcrafting.core.crafting;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.core.Crux;
import killercreepr.cruxcrafting.api.crafting.CruxCraftingRecipeManager;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxCraftingRecipe;
import killercreepr.cruxcrafting.core.entity.memory.RecipesHolder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class LimitedCraftingRecipeManager extends SimpleRecipeManager<CruxCraftingRecipe> implements CruxCraftingRecipeManager {
    protected final Key memoryHolderKey;

    public LimitedCraftingRecipeManager(Key key) {
        super(key);
        memoryHolderKey = Crux.key(key.namespace() + ":holder/" + key.value());
    }


    public Key getMemoryHolderKey() {
        return memoryHolderKey;
    }

    @Override
    public boolean hasRecipe(@NotNull Entity entity, @NotNull CruxCraftingRecipe recipe) {
        if(!(recipe instanceof Keyed k)) return false;
        if(!hasRecipe(k.key())) return false;
        RecipesHolder holder = recipesHolder(entity);
        return holder != null && holder.hasRecipe(k.key());
    }

    @Override
    public boolean grantRecipe(@NotNull Entity entity, @NotNull CruxCraftingRecipe recipe) {
        if(!(recipe instanceof Keyed k)) return false;
        if(!hasRecipe(k.key())) return false;
        RecipesHolder holder = recipesHolder(entity);
        return holder != null && holder.grantRecipe(k.key());
    }

    @Override
    public boolean revokeRecipe(@NotNull Entity entity, @NotNull CruxCraftingRecipe recipe) {
        if(!(recipe instanceof Keyed k)) return false;
        if(!hasRecipe(k.key())) return false;
        RecipesHolder holder = recipesHolder(entity);
        return holder != null && holder.revokeRecipe(k.key());
    }

    public RecipesHolder recipesHolder(Entity entity){
        var mem = EntityMemory.get(entity);
        if(mem == null) return null;
        return mem.getDataHolders().get(RecipesHolder.class, memoryHolderKey);
    }
}
