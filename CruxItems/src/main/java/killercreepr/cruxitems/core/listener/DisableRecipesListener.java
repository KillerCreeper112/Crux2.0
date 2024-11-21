package killercreepr.cruxitems.core.listener;

import killercreepr.crux.core.util.CruxItem;
import killercreepr.cruxitems.core.item.CruxedItem;
import net.kyori.adventure.key.Key;
import org.bukkit.Keyed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;
import org.jetbrains.annotations.Nullable;

public class DisableRecipesListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        if(event.getRecipe() instanceof Keyed k && !k.getKey().namespace().equalsIgnoreCase(Key.MINECRAFT_NAMESPACE)) return;
        CraftingInventory inv = event.getInventory();
        for(ItemStack item : inv.getMatrix()){
            if(check(item)){
                inv.setResult(null);
                return;
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPrepareSmithing(PrepareSmithingEvent event) {
        SmithingInventory inv = event.getInventory();
        if(check(inv.getInputTemplate()) || check(inv.getInputMineral())){
            event.setResult(null);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory inv = event.getInventory();
        if(check(inv.getSecondItem())){
            event.setResult(null);
        }
    }

    public boolean check(@Nullable ItemStack item){
        if(CruxItem.isEmpty(item)) return false;
        if(new CruxedItem(item).isPluginItem()){
            return true;
        }
        return false;
    }
}
