package killercreepr.cruxitems.core.listener;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.item.ItemLootTable;
import killercreepr.crux.core.component.SimpleBlockComponentWrapper;
import killercreepr.crux.core.util.CruxCollection;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxitems.core.component.CruxItemsComponents;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Vault;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseLootEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.VaultDisplayItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class OtherInteractionListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block b = event.getBlockPlaced();
        ItemStack item = event.getItemInHand();
        CruxItem cruxItem = CruxItem.wrap(item);

        var state = b.getState();
        var components = new SimpleBlockComponentWrapper(state);
        var spawnerData = cruxItem.get(CruxItemsComponents.VAULT_BLOCK_KEY);
        if(spawnerData != null){
            components.set(CruxItemsComponents.VAULT_BLOCK_KEY, spawnerData);
        }

        var lootTable = cruxItem.get(CruxItemsComponents.DISPENSE_BLOCK_LOOT_TABLE);
        if(lootTable != null){
            components.set(CruxItemsComponents.DISPENSE_BLOCK_LOOT_TABLE, lootTable);
        }

        lootTable = cruxItem.get(CruxItemsComponents.OMINOUS_DISPENSE_BLOCK_LOOT_TABLE);
        if(lootTable != null){
            components.set(CruxItemsComponents.OMINOUS_DISPENSE_BLOCK_LOOT_TABLE, lootTable);
        }


        if(spawnerData == null || !(state instanceof Vault vault)) return;

        vault.setKeyItem(spawnerData.value());
        vault.update();
    }

    public boolean isOminous(Block b){
        return b.getBlockData() instanceof org.bukkit.block.data.type.Vault v && v.isOminous();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onVaultDisplayItem(VaultDisplayItemEvent event) {
        var components = new SimpleBlockComponentWrapper(event.getBlock().getState());
        boolean ominous = isOminous(event.getBlock());
        var lootTableData = components.get(
            ominous ? CruxItemsComponents.OMINOUS_DISPENSE_BLOCK_LOOT_TABLE : CruxItemsComponents.DISPENSE_BLOCK_LOOT_TABLE
        );
        if(lootTableData == null) return;
        if(!lootTableData.isOverrideVanilla() && CruxMath.random().nextBoolean()) return;
        event.setDisplayItem(getRandomItem(event, lootTableData.getLootTable()));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockDispenseLoot(BlockDispenseLootEvent event) {
        Block b = event.getBlock();
        var components = new SimpleBlockComponentWrapper(b.getState());
        boolean ominous = isOminous(event.getBlock());
        var lootTableData = components.get(
            ominous ? CruxItemsComponents.OMINOUS_DISPENSE_BLOCK_LOOT_TABLE : CruxItemsComponents.DISPENSE_BLOCK_LOOT_TABLE
        );
        if(lootTableData == null) return;

        if(lootTableData.isOverrideVanilla()) event.setDispensedLoot(null);

        var table = lootTableData.getLootTable();
        event.getDispensedLoot().addAll(table.populateLoot(
            LootContext.builder()
                .location(b.getLocation())
                .looted(b)
                .looter(event.getPlayer())
                .build()
        ));
    }


    public ItemStack getRandomItem(VaultDisplayItemEvent event, ItemLootTable table){
        Block b = event.getBlock();
        return CruxCollection.getRandom(table.populateLoot(
            LootContext.builder()
                .location(b.getLocation())
                .looted(b)
                .build()
        ));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(!event.getAction().isRightClick()) return;
        Block b = event.getClickedBlock();
        if(b == null) return;
        ItemStack item = event.getItem();
        if(CruxItem.isEmpty(item)) return;
        BlockState state = b.getState();
        if(!(state instanceof Vault vault)) return;

        var components = new SimpleBlockComponentWrapper(state);
        var validKey = components.get(CruxItemsComponents.VAULT_BLOCK_KEY);
        if(validKey == null) return;
        vault.setKeyItem(validKey.value());
        vault.update();
    }

}
