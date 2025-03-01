package killercreepr.cruxcrafting.api.crafting.crafter;

import killercreepr.crux.core.util.CruxInv;
import killercreepr.cruxcrafting.api.crafting.CruxCraftingMatrix;
import killercreepr.cruxcrafting.api.crafting.CruxCraftingRecipeManager;
import killercreepr.cruxcrafting.api.crafting.context.CruxCraftingRecipeContext;
import killercreepr.cruxcrafting.api.crafting.context.CruxIngredientContext;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxCraftingRecipe;
import killercreepr.cruxcrafting.core.crafting.crafter.CraftingInvCrafter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public interface CruxCraftingCrafter extends CruxCrafter{
    static CraftingInvCrafter craftingCrafter(CruxCraftingRecipeManager manager, CraftingInventory inv){
        return new CraftingInvCrafter(manager, inv);
    }

    static CraftingInvCrafter craftingCrafterDelayedUpdate(CruxCraftingRecipeManager manager, CraftingInventory inv,
                                                           Plugin plugin, int delay){
        return new CraftingInvCrafter(manager, inv, plugin, delay);
    }

    CruxCraftingRecipe getRecipe(CruxCraftingMatrix matrix);
    ItemStack[] getMatrix();
    void setItem(int slot, ItemStack item);
    void setResults(List<ItemStack> items);
    List<ItemStack> getResults();
    boolean isResultSlot(int slot);

    void handleCrafting(InventoryClickEvent event);
    void onHandleCraftingEnd();
    void updateCraftingInv();

    default boolean hasSpaceForAll(HumanEntity p, List<ItemStack> items){
        for(ItemStack i : items){
            if(!CruxInv.hasSpace(p.getInventory(), i)) return false;
        }
        return true;
    }

    default boolean allEquals(List<ItemStack> original, List<ItemStack> list){
        for(int i = 0; i < original.size(); i++){
            if(i >= list.size()) return false;
            ItemStack og = original.get(i);
            ItemStack check = list.get(i);
            if(!og.equals(check)) return false;
        }
        return true;
    }

    default List<ItemStack> copy(List<ItemStack> list){
        List<ItemStack> newList = new ArrayList<>();
        for(ItemStack i : list){
            newList.add(i.clone());
        }
        return newList;
    }

    default void removeIngredients(CruxCraftingRecipeContext ctx, CruxCraftingRecipe recipe){
        recipe.mapIngredients(ctx.craftingMatrix()).forEach((slot, ingredient) ->{
            ItemStack item = ctx.craftingMatrix().getItem(slot).clone();
            //if(CruxItem.isEmpty(item)) return;
            //ingredient.onRemove(item);
            slot++;
            setItem(slot, ingredient.removeItem(CruxIngredientContext.ingredientContext(item, ctx)));
        });
    }
}
