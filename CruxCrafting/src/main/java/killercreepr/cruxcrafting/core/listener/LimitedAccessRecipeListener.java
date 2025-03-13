package killercreepr.cruxcrafting.core.listener;

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.core.Crux;
import killercreepr.cruxcrafting.api.crafting.CruxCraftingRecipeManager;
import killercreepr.cruxcrafting.api.crafting.context.CruxIngredientContext;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxCraftingRecipe;
import killercreepr.cruxcrafting.api.event.EntityDiscoverRecipeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class LimitedAccessRecipeListener implements Listener {
    protected final CruxCraftingRecipeManager recipeManager;

    public LimitedAccessRecipeListener(CruxCraftingRecipeManager recipeManager) {
        this.recipeManager = recipeManager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInventorySlotChange(PlayerInventorySlotChangeEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getNewItemStack();
        if(CruxItem.isEmpty(item)) return;
        ItemStack old = event.getOldItemStack();
        if(!CruxItem.isEmpty(old) && Crux.handlers().item().getType(old).equals(
            Crux.handlers().item().getType(item)
        )) return;
        recipeManager.forEach(recipe ->{
            if(recipeManager.hasRecipe(p, recipe)) return;
            if(!hasRecipeIngredient(item, recipe)) return;

            EntityDiscoverRecipeEvent discoverEvent = new EntityDiscoverRecipeEvent(p, recipeManager, recipe);
            if(!discoverEvent.callEvent()) return;

            recipeManager.grantRecipe(p, recipe);
        });
    }

    public boolean hasRecipeIngredient(ItemStack item,CruxCraftingRecipe recipe){
        for (CruxRecipeIngredient ingredient : recipe.ingredients()) {
            if(ingredient.test(CruxIngredientContext.ingredientContext(item))) return true;
        }
        return false;
    }

}
