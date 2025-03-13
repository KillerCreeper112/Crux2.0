package killercreepr.cruxcrafting.core.menu;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.cruxcrafting.api.crafting.context.CruxIngredientContext;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxCraftingRecipe;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CraftingTableRecipeViewer extends CraftingRecipeMenuViewer{
    public CraftingTableRecipeViewer(Inventory inv, CruxCraftingRecipe recipe) {
        super(inv, recipe);
    }

    @Override
    public void display() {
        this.setIngredients(this.recipe.mapIngredientDisplay(this.craftingWidth(), this.craftingHeight()));
    }

    @Override
    public void setIngredient(int index, CruxRecipeIngredient ingredient) {
        for(HumanEntity viewer : this.inv.getViewers()){
            int slot = this.calculateSlotFromIngredientIndex(index);
            CruxIngredientContext ctx = findIngredient(viewer.getInventory().getContents(), ingredient);
            if(ctx == null) setIngredientDisplay(slot, null);
            else{
                ItemStack item = ctx.ingredient();
                ItemStack clone = item.clone();
                ingredient.removeItem(ctx);
                int difference = clone.getAmount() - item.getAmount();
                clone.setAmount(difference);
                setIngredientDisplay(slot, clone);
            }
        }
    }

    public CruxIngredientContext findIngredient(ItemStack[] inv, CruxRecipeIngredient ingredient){
        for(ItemStack item : inv){
            if(CruxItem.isEmpty(item)) continue;
            CruxIngredientContext ctx = CruxIngredientContext.ingredientContext(item);
            if(!ingredient.test(ctx)) continue;
            return ctx;
        }
        return null;
    }

    @Override
    public int[] getResultSlots() {
        return new int[]{0};
    }

    @Override
    public int[] getIngredientSlots(){
        return new int[]{
            1, 2, 3,
            4, 5, 6,
            7, 8, 9
        };
    }
}
