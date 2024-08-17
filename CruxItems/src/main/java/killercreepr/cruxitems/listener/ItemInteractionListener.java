package killercreepr.cruxitems.listener;

import killercreepr.crux.util.CruxItem;
import killercreepr.cruxitems.item.CruxedItem;
import killercreepr.cruxitems.item.interaction.InteractableItem;
import killercreepr.cruxitems.item.interaction.ItemUseContext;
import killercreepr.cruxitems.item.interaction.impl.ItemUseContextImpl;
import killercreepr.cruxitems.item.interaction.ItemUseResult;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ItemInteractionListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if(CruxItem.isEmpty(item)) return;
        EquipmentSlot hand = event.getHand();
        if(hand==null) return;
        CruxedItem crux = new CruxedItem(item);
        if(!(crux.getPluginItem() instanceof InteractableItem pluginItem)) return;

        Player p = event.getPlayer();
        ItemUseContext ctx = ItemUseContextImpl.builder()
            .player(p)
            .blockClicked(event.getClickedBlock())
            .action(event.getAction())
            .blockFace(event.getBlockFace())
            .clickedPosition(event.getInteractionPoint())
            .hand(hand)
            .item(crux)
            .build();

        ItemUseResult result = pluginItem.onUse(ctx);
        if(result.isCancelled()) event.setCancelled(true);
        Event.Result r = result.getUseInteractedBlock();
        if(r != null) event.setUseInteractedBlock(r);
        r = result.getUseItemInHand();
        if(r != null) event.setUseItemInHand(r);
    }

}
