package killercreepr.cruxbrewery.recipe;

import killercreepr.crux.core.Crux;
import killercreepr.crux.core.util.CruxItem;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/*
 * Slot 0: 3 Potion Slot Far Left
 * Slot 1: 3 Potion Slot Middle
 * Slot 2: 3 Potion Slot Far Right
 * Slot 3: Ingredient Slot 4: Fuel
 */
public class BrewClock extends BukkitRunnable {
    private final BrewerInventory inventory;
    private final BrewRecipe recipe;
    private final ItemStack[] before;
    private final BrewingStand stand;
    private int current;
    public BrewClock(BrewRecipe recipe, BrewerInventory inventory, int time) {
        this.recipe = recipe;
        this.inventory = inventory;
        this.stand = inventory.getHolder();
        this.before = inventory.getContents().clone();
        this.current = time;
        runTaskTimer(Crux.getMainPlugin(), 0L, 1L);
    }
    @Override
    public void run() {
        if (current == 0) {
            cancel();
            // Set ingredient to 1 less than the current. Otherwise set to air
            ItemStack ingredient = inventory.getIngredient();
            if(CruxItem.isEmpty(ingredient)) return;
            ingredient.setAmount(inventory.getIngredient().getAmount() - 1);
            // Brew recipe for each item put in
            for (int i = 0; i < 3; i++) {
                ItemStack item = inventory.getItem(i);
                if(CruxItem.isEmpty(item)) continue;
                recipe.brew(inventory, item, ingredient);
            }
            return;
        }
        // If a player drags an item, fuel, or any contents, reset it
        if (searchChanged(before, inventory.getContents())) {
            cancel();
            return;
        }
        // Decrement, set the brewing time, and update the stand
        current--;
        stand.setBrewingTime(current);
        stand.update(true);
    }
    // Check if any slots were changed
    public boolean searchChanged(ItemStack[] before, ItemStack[] after) {
        for (int i = 0; i < before.length; i++) {
            if ((before[i] != null && after[i] == null) || (before[i] == null && after[i] != null)) {
                return false;
            } else {
                ItemStack check = before[i];
                if(check==null) check = new ItemStack(Material.AIR);
                if (!check.isSimilar(after[i])) {
                    return false;
                }
            }
        }
        return true;
    }
}

