package killercreepr.cruxitems.core.listener;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.item.predicate.ItemPredicate;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.SimpleBlockComponentWrapper;
import killercreepr.cruxitems.core.component.CruxItemsComponents;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Vault;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class OtherInteractionListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block b = event.getBlockPlaced();
        ItemStack item = event.getItemInHand();
        CruxItem cruxItem = CruxItem.wrap(item);

        var spawnerData = cruxItem.get(CruxItemsComponents.VAULT_BLOCK_KEY);
        if(spawnerData == null) return;

        var components = new SimpleBlockComponentWrapper(b.getState());
        components.set(CruxItemsComponents.VAULT_BLOCK_KEY, spawnerData);
    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(!event.getAction().isRightClick()) return;
        Block b = event.getClickedBlock();
        if(b == null) return;
        ItemStack item = event.getItem();
        if(CruxItem.isEmpty(item)) return;
        BlockState state = b.getState();
        if(!(state instanceof Vault)) return;
        Player p = event.getPlayer();

        var components = new SimpleBlockComponentWrapper(state);
        ItemPredicate validKeys = components.get(CruxItemsComponents.VAULT_BLOCK_KEY);
        if(validKeys == null) return;
        if(!validKeys.test(item)){
            event.setCancelled(true);
            p.sendMessage(Crux.format().deserialize("<red>This item cannot be used to open this vault."));
        }
    }

}
