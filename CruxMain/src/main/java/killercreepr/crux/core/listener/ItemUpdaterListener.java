package killercreepr.crux.core.listener;

import killercreepr.crux.api.event.CruxItemLootGenerateEvent;
import killercreepr.crux.core.Crux;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseLootEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.SmithItemEvent;
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

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCrafterCraft(CrafterCraftEvent event) {
        event.setResult(Crux.handlers().item().update(event.getResult()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        var result = event.getInventory().getResult();
        if(result == null) return;
        event.getInventory().setResult(Crux.handlers().item().update(result));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDropItem(EntityDropItemEvent event) {
        event.getItemDrop().setItemStack(
            Crux.handlers().item().update(event.getItemDrop().getItemStack())
        );
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockDropItem(BlockDropItemEvent event) {
        for (Item item : event.getItems()) {
            item.setItemStack(Crux.handlers().item().update(item.getItemStack()));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEnchantItem(EnchantItemEvent event) {
        Crux.handlers().item().update(event.getItem());
    }
}
