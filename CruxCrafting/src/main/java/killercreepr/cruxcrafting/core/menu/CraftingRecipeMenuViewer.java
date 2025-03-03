package killercreepr.cruxcrafting.core.menu;

import killercreepr.crux.core.Crux;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxCraftingRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class CraftingRecipeMenuViewer {
    protected final Inventory inv;
    protected final CruxCraftingRecipe recipe;
    public CraftingRecipeMenuViewer(Inventory inv, CruxCraftingRecipe recipe) {
        this.inv = inv;
        this.recipe = recipe;
    }

    public int craftingWidth(){
        return 3;
    }

    public int craftingHeight(){
        return 3;
    }

    public void display(){
        setIngredients(recipe.mapIngredientDisplay(
            craftingWidth(), craftingHeight()
        ));
        setResults(recipe.getDisplayedResultItems());
    }

    public int[] getResultSlots(){
        return new int[]{15};
    }

    public void setResultDisplay(int slot, @Nullable ItemStack display){
        inv.setItem(slot, display == null ? null : Crux.handlers().item().update(display));
    }

    public void setIngredientDisplay(int slot, @Nullable ItemStack display){
        inv.setItem(slot, display == null ? null : Crux.handlers().item().update(display));
    }

    public void setResults(@Nullable List<ItemStack> results){
        if(results == null || results.isEmpty()){
            for(int slot : getResultSlots()){
                setResultDisplay(slot, null);
            }
            return;
        }
        int index = 0;
        for(int slot : getResultSlots()){
            setResultDisplay(slot, index < results.size() ? results.get(index) : null);
            index++;
        }
    }

    public void setIngredients(Map<Integer, CruxRecipeIngredient> ingredients){
        ingredients.forEach(this::setIngredient);
    }

    public int calculateSlotFromIngredientIndex(int index){
        if(index <= 2) return index + 1;
        if(index <= 5) return index + 7;
        return index + 13;
    }

    public void setIngredient(int index, CruxRecipeIngredient ingredient){
        List<ItemStack> display = ingredient.getItemDisplay();
        int slot = calculateSlotFromIngredientIndex(index);
        if(display==null || display.isEmpty()){
            setIngredientDisplay(slot, null);
            return;
        }
        setIngredientDisplay(slot, display.getFirst());
    }
}
