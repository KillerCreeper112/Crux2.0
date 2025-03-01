package killercreepr.cruxenchants.core.listener;

import killercreepr.cruxenchants.api.crafting.CruxCraftingRecipeManager;
import killercreepr.cruxenchants.api.crafting.crafter.CruxCraftingCrafter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.plugin.Plugin;

public class CraftingListener implements Listener {
    protected final Plugin plugin;
    protected final CruxCraftingRecipeManager craftingManager;

    public CraftingListener(Plugin plugin, CruxCraftingRecipeManager craftingManager) {
        this.plugin = plugin;
        this.craftingManager = craftingManager;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getClickedInventory() == null) return;
        if((event.getView().getTopInventory() instanceof CraftingInventory inv)){
            CruxCraftingCrafter crafter = CruxCraftingCrafter.craftingCrafterDelayedUpdate(craftingManager, inv, plugin, 1);
            crafter.handleCrafting(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
        if((event.getView().getTopInventory() instanceof CraftingInventory inv)){
            CruxCraftingCrafter crafter = CruxCraftingCrafter.craftingCrafter(craftingManager, inv);
            plugin.getServer().getScheduler().runTaskLater(plugin, () ->{
                crafter.updateCraftingInv();
            }, 1L);
        }
    }


    /*public CruxCraftingRecipe getRecipe(CruxCraftingMatrix matrix){
        CruxCraftingRecipeContext ctx = CruxRecipeContext.craftingRecipeContext(matrix);
        for(CruxCraftingRecipe recipe : craftingManager){
            CruxRecipeResult result = recipe.testResult(ctx);
            if(result == null) continue;
            return recipe;
        }
        return null;
    }

    public void removeIngredients(CraftingInventory inv, CruxCraftingRecipeContext ctx, CruxCraftingRecipe recipe){
        recipe.mapIngredients(ctx.craftingMatrix()).forEach((slot, ingredient) ->{
            ItemStack item = ctx.craftingMatrix().getItem(slot).clone();
            //if(CruxItem.isEmpty(item)) return;
            //ingredient.onRemove(item);
            slot++;
            inv.setItem(slot, ingredient.removeItem(CruxIngredientContext.ingredientContext(item, ctx)));
        });
    }

    public void handleCrafting(InventoryClickEvent event, CraftingInventory inv){
        int slot = event.getRawSlot();
        if(slot == 0){
            ItemStack result = event.getCurrentItem();
            if(CruxItem.isEmpty(result)) return;
            CruxCraftingMatrix matrix = CruxCraftingMatrix.craftingMatrix(inv.getMatrix());
            CruxCraftingRecipe recipe = getRecipe(matrix);
            if(recipe == null) return;

            event.setCancelled(true);
            ClickType clickType = event.getClick();
            HumanEntity p = event.getWhoClicked();
            if(clickType.isShiftClick()){
                ItemStack originalResult = result.clone();
                while(originalResult.equals(result) && CruxInv.hasSpace(p.getInventory(), result)){
                    p.getInventory().addItem(result.clone()).values().forEach(drop -> p.getWorld().dropItem(p.getLocation(), drop));
                    inv.setResult(null);
                    CruxCraftingRecipeContext ctx = CruxRecipeContext.craftingRecipeContext(matrix);
                    removeIngredients(inv, ctx, recipe);
                    updateCraftingInv(inv);
                    result = inv.getResult();
                    matrix = CruxCraftingMatrix.craftingMatrix(inv.getMatrix());
                }
                return;
            }

            CruxCraftingRecipeContext ctx = CruxRecipeContext.craftingRecipeContext(matrix);
            ItemStack cursor = event.getCursor();
            if(CruxItem.isEmpty(cursor)){
                p.setItemOnCursor(result);
                inv.setResult(null);
                removeIngredients(inv, ctx, recipe);
                updateCraftingInv(inv);
                return;
            }
            if(!cursor.isSimilar(result)) return;
            int amount = cursor.getAmount() + result.getAmount();
            if(amount > cursor.getMaxStackSize()) return;
            cursor.setAmount(cursor.getAmount() + result.getAmount());
            inv.setResult(null);
            removeIngredients(inv, ctx, recipe);
            updateCraftingInv(inv);
            return;
        }

        plugin.getServer().getScheduler().runTaskLater(plugin, () ->{
            updateCraftingInv(inv);
        }, 1L);
    }

    public void updateCraftingInv(CraftingInventory inv){
        CruxCraftingMatrix matrix = CruxCraftingMatrix.craftingMatrix(inv.getMatrix());
        CruxCraftingRecipeContext ctx = CruxRecipeContext.craftingRecipeContext(matrix);
        for(CruxCraftingRecipe recipe : craftingManager){
            CruxRecipeResult result = recipe.testResult(ctx);
            if(result == null) continue;
            List<ItemStack> itemResults = result.getResultItems();
            if(itemResults.isEmpty()) continue;
            ItemStack resultItem = itemResults.getFirst();
            inv.setResult(resultItem);
            return;
        }
    }*/

}
