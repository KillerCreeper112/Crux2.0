package killercreepr.cruxitems.listener;

import killercreepr.crux.util.CruxItem;
import killercreepr.cruxitems.item.CruxedItem;
import killercreepr.cruxitems.item.interaction.InteractableItem;
import killercreepr.cruxitems.item.interaction.ItemUseContext;
import killercreepr.cruxitems.item.interaction.ItemUseResult;
import killercreepr.cruxitems.item.interaction.impl.ItemUseContextImpl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ItemInteractionListener implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player p)) return;
        ItemStack item = p.getInventory().getItemInMainHand();
        CruxedItem crux = new CruxedItem(item);
        if(!(crux.getPluginItem() instanceof InteractableItem pluginItem)) return;

        ItemUseContext ctx = ItemUseContextImpl.builder()
            .player(p)
            .blockClicked(null)
            .action(Action.LEFT_CLICK_AIR)
            .blockFace(null)
            .clickedPosition(null)
            .hand(EquipmentSlot.HAND)
            .entityClicked(event.getEntity())
            .item(crux)
            .build();

        ItemUseResult result = pluginItem.onUse(ctx);
        Boolean cancel = result.getCancelled();
        if(cancel != null) event.setCancelled(cancel);
    }


    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        Entity rightClicked = event.getRightClicked();
        ItemStack item = p.getInventory().getItem(event.getHand());

        CruxedItem crux = new CruxedItem(item);
        if(!(crux.getPluginItem() instanceof InteractableItem pluginItem)) return;

        ItemUseContext ctx = ItemUseContextImpl.builder()
            .player(p)
            .blockClicked(null)
            .action(Action.RIGHT_CLICK_AIR)
            .blockFace(null)
            .clickedPosition(null)
            .hand(event.getHand())
            .entityClicked(rightClicked)
            .item(crux)
            .build();

        ItemUseResult result = pluginItem.onUse(ctx);
        Boolean cancel = result.getCancelled();
        if(cancel != null) event.setCancelled(cancel);
    }

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
        Boolean cancel = result.getCancelled();
        if(cancel != null) event.setCancelled(cancel);
        Event.Result r = result.getUseInteractedBlock();
        if(r != null) event.setUseInteractedBlock(r);
        r = result.getUseItemInHand();
        if(r != null) event.setUseItemInHand(r);
    }

}
