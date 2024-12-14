package killercreepr.crux.core.listener;

import killercreepr.crux.api.component.serialization.PersistHolderComponentHandler;
import killercreepr.crux.api.component.serialization.PersistentDataWrappers;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.item.ItemLootTable;
import killercreepr.crux.core.component.CruxComponents;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
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
        List<ItemLootTable> lootTable = CruxItem.wrap(item).get(CruxComponents.ITEM_LOOT_TABLES);
        if(lootTable == null) return;
        Block b = event.getBlock();
        BlockState state = b.getState();
        if(!(state instanceof PersistentDataHolder)) return;
        PersistHolderComponentHandler handler = PersistentDataWrappers.wrapBlockState(state);
        handler.set(CruxComponents.ITEM_LOOT_TABLES, lootTable);
        /*CruxPersist.ITEM_LOOT_TABLES.set(c, lootTable);
        state.update();*/
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        Inventory inv = event.getInventory();
        if(inv.getLocation() == null) return;
        Block b = inv.getLocation().getBlock();
        BlockState state = b.getState();
        if(!(state instanceof PersistentDataHolder data)) return;
        HumanEntity p = event.getPlayer();

        PersistHolderComponentHandler handler = PersistentDataWrappers.wrapBlockState(state);

        List<ItemLootTable> lootTable = handler.get(CruxComponents.ITEM_LOOT_TABLES);
        if(lootTable == null) return;
        Long time = handler.get(CruxComponents.LOOT_GENERATED_TIME);
        if(time != null) return;
        handler.set(CruxComponents.LOOT_GENERATED_TIME, System.currentTimeMillis());
        state.update();

        LootContext ctx = LootContext.builder().looted(b).location(inv.getLocation()).looter(p).build();
        lootTable.forEach(loot -> loot.fillInventory(inv, ctx));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockDropItem(BlockDropItemEvent event) {
        BlockState state = event.getBlockState();
        if(!(state instanceof PersistentDataHolder data)) return;
        HumanEntity p = event.getPlayer();

        PersistHolderComponentHandler handler = PersistentDataWrappers.wrapBlockState(state);

        List<ItemLootTable> lootTable = handler.get(CruxComponents.ITEM_LOOT_TABLES);
        if(lootTable == null) return;
        Long time = handler.get(CruxComponents.LOOT_GENERATED_TIME);
        if(time != null) return;
        Block b = event.getBlock();
        Location spawn = b.getLocation().toCenterLocation();

        World world = b.getWorld();

        LootContext ctx = LootContext.builder().looted(b).location(b.getLocation()).looter(p).build();
        List<Item> itemDrops = event.getItems();
        lootTable.forEach(loot -> loot.populateLoot(ctx).forEach(item ->{
            itemDrops.add(world.dropItem(spawn, item));
        }));
    }

}
