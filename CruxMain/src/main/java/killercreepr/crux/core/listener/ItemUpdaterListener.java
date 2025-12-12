package killercreepr.crux.core.listener;

import killercreepr.crux.api.event.CruxItemLootGenerateEvent;
import killercreepr.crux.core.Crux;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseLootEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;

public class ItemUpdaterListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCruxItemLootGenerate(CruxItemLootGenerateEvent event) {
        for (ItemStack item : event.getLoot()) {
            Crux.handlers().item().update(item);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLootGenerate(LootGenerateEvent event) {
        for (ItemStack item : event.getLoot()) {
            Crux.handlers().item().update(item);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        for (ItemStack item : event.getDrops()) {
            Crux.handlers().item().update(item);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockDispenseLoot(BlockDispenseLootEvent event) {
        for (ItemStack item : event.getDispensedLoot()) {
            Crux.handlers().item().update(item);
        }
    }

}
