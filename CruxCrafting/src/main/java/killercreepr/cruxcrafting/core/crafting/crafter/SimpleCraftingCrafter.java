package killercreepr.cruxcrafting.core.crafting.crafter;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.core.util.CruxEntityUtil;
import killercreepr.cruxcrafting.api.crafting.CruxCraftingMatrix;
import killercreepr.cruxcrafting.api.crafting.CruxCraftingRecipeManager;
import killercreepr.cruxcrafting.api.crafting.CruxRecipeResult;
import killercreepr.cruxcrafting.api.crafting.context.CruxCraftingRecipeContext;
import killercreepr.cruxcrafting.api.crafting.context.CruxRecipeContext;
import killercreepr.cruxcrafting.api.crafting.crafter.CruxCraftingCrafter;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxCraftingRecipe;
import killercreepr.cruxcrafting.api.event.PlayerCruxCraftEvent;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class SimpleCraftingCrafter implements CruxCraftingCrafter {
    protected final CruxCraftingRecipeManager craftingManager;
    protected final Inventory inv;

    public SimpleCraftingCrafter(CruxCraftingRecipeManager craftingManager, Inventory inv) {
        this.craftingManager = craftingManager;
        this.inv = inv;
    }

    public CruxCraftingRecipe getRecipe(CruxCraftingMatrix matrix){
        CruxCraftingRecipeContext ctx = CruxRecipeContext.craftingRecipeContext(matrix);
        for(CruxCraftingRecipe recipe : craftingManager){
            CruxRecipeResult result = recipe.testResult(ctx);
            if(result == null) continue;
            return recipe;
        }
        return null;
    }

    public void setItem(int slot, ItemStack item){
        inv.setItem(slot, item);
    }

    public ItemStack[] getMatrix(){
        return inv.getContents();
    }

    public boolean isResultSlot(int slot){
        return slot == 0;
    }

    public void handleCrafting(InventoryClickEvent event){
        int slot = event.getRawSlot();
        if(isResultSlot(slot)){
            ItemStack clicked = event.getCurrentItem();
            if(CruxItem.isEmpty(clicked)) return;
            CruxCraftingMatrix matrix = CruxCraftingMatrix.craftingMatrix(getMatrix());
            CruxCraftingRecipe recipe = getRecipe(matrix);
            if(recipe == null) return;

            event.setCancelled(true);
            ClickType clickType = event.getClick();
            HumanEntity p = event.getWhoClicked();
            List<ItemStack> results = getResults();
            if(clickType.isShiftClick() || results.size() > 1){
                //todo
                List<ItemStack> originalResults = copy(getResults());
                while(allEquals(originalResults, results) && hasSpaceForAll(p, results)){
                    CruxEntityUtil.giveOrDrop(p, results);
                    setResults(null);
                    CruxCraftingRecipeContext ctx = CruxRecipeContext.craftingRecipeContext(matrix);
                    removeIngredients(ctx, recipe);
                    updateCraftingInv();
                    results = getResults();
                    matrix = CruxCraftingMatrix.craftingMatrix(getMatrix());
                }
                return;
            }

            CruxCraftingRecipeContext ctx = CruxRecipeContext.craftingRecipeContext(matrix);
            ItemStack cursor = event.getCursor();
            if(CruxItem.isEmpty(cursor)){
                PlayerCruxCraftEvent craftEvent = new PlayerCruxCraftEvent(p, recipe, this, results);
                if(!craftEvent.callEvent()) return;

                if(results.size() > 1){
                    CruxEntityUtil.giveOrDrop(p, results);
                }else if(!results.isEmpty()) p.setItemOnCursor(results.getFirst());

                setResults(null);
                removeIngredients(ctx, recipe);
                updateCraftingInv();
                return;
            }
            ItemStack result = results.isEmpty() ? null : results.getFirst();
            if(result != null && !cursor.isSimilar(result)) return;
            int amount = cursor.getAmount() + (result == null ? 0 : result.getAmount());
            if(amount > cursor.getMaxStackSize()) return;

            PlayerCruxCraftEvent craftEvent = new PlayerCruxCraftEvent(p, recipe, this, results);
            if(!craftEvent.callEvent()) return;

            if(results.size() > 1){
                CruxEntityUtil.giveOrDrop(p, results);
            }else if(!results.isEmpty()){
                result = results.getFirst();
                amount = cursor.getAmount() + (result == null ? 0 : result.getAmount());
                if(amount > cursor.getMaxStackSize()){
                    CruxEntityUtil.giveOrDrop(p, results);
                }else cursor.setAmount(amount);
            }
            setResults(null);
            removeIngredients(ctx, recipe);
            updateCraftingInv();
            return;
        }
        onHandleCraftingEnd();
    }

    public void onHandleCraftingEnd(){
        /*plugin.getServer().getScheduler().runTaskLater(plugin, () ->{
            updateCraftingInv(inv);
        }, 1L);*/
        updateCraftingInv();
    }

    public boolean clearResultsBeforeUpdating(){
        return true;
    }

    public void updateCraftingInv(){
        CruxCraftingMatrix matrix = CruxCraftingMatrix.craftingMatrix(getMatrix());
        CruxCraftingRecipeContext ctx = CruxRecipeContext.craftingRecipeContext(matrix);
        if(clearResultsBeforeUpdating()) setResults(null);
        for(CruxCraftingRecipe recipe : craftingManager){
            CruxRecipeResult result = recipe.testResult(ctx);
            if(result == null) continue;
            List<ItemStack> itemResults = result.getResultItems();
            if(itemResults.isEmpty()) continue;
            setResults(itemResults);
            return;
        }
    }
}
