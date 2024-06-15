package killercreepr.cruxbrewery.listener;

import killercreepr.crux.util.CruxItem;
import killercreepr.cruxbrewery.recipe.BrewClock;
import killercreepr.cruxbrewery.recipe.BrewRecipe;
import killercreepr.cruxbrewery.registries.CruxBreweryRegistries;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class BrewingStandListener implements Listener {
    protected final Plugin plugin;

    public BrewingStandListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if(!(event.getView().getTopInventory() instanceof BrewerInventory inv)) return;
        ItemStack item = event.getCursor();
        if(CruxItem.isEmpty(item)) return;

        if(event.getClickedInventory() instanceof BrewerInventory clickedIn){
            event.setCancelled(true);
            clickedIn.setItem(event.getSlot(), item);
            event.getWhoClicked().setItemOnCursor(null);
        }
        plugin.getServer().getScheduler().runTask(plugin, task ->{
            BrewRecipe recipe = CruxBreweryRegistries.getBrewRecipe(inv);
            if(recipe != null){
                new BrewClock(recipe, inv, recipe.getBrewTime());
            }
        });
    }
}
