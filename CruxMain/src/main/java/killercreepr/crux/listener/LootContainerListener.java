package killercreepr.crux.listener;

import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.item.ItemLootTable;
import killercreepr.crux.persistence.CruxPersist;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import java.util.List;

public class LootContainerListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        List<ItemLootTable> lootTable = CruxPersist.ITEM_LOOT_TABLES.get(item, null);
        if(lootTable == null) return;
        Block b = event.getBlock();
        BlockState state = b.getState();
        if(!(state instanceof PersistentDataHolder c)) return;
        CruxPersist.ITEM_LOOT_TABLES.set(c, lootTable);
        state.update();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        Inventory inv = event.getInventory();
        if(inv.getLocation() == null) return;
        Block b = inv.getLocation().getBlock();
        BlockState state = b.getState();
        if(!(state instanceof PersistentDataHolder data)) return;
        HumanEntity p = event.getPlayer();
        List<ItemLootTable> lootTable = CruxPersist.ITEM_LOOT_TABLES.get(data, null);
        if(lootTable == null) return;
        Long time = CruxPersist.LOOT_GENERATED_TIME.get(data, null);
        if(time != null) return;
        CruxPersist.LOOT_GENERATED_TIME.set(data, System.currentTimeMillis());
        state.update();

        LootContext ctx = LootContext.builder().looted(b).location(inv.getLocation()).looter(p).build();
        lootTable.forEach(loot -> loot.fillInventory(inv, ctx));
    }
}
