package killercreepr.cruxitems.core.listener;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.cruxitems.api.event.ConsumableComponentUseEvent;
import killercreepr.cruxitems.api.event.CustomItemPreConsumeEvent;
import killercreepr.cruxitems.api.event.CustomItemPreUseEvent;
import killercreepr.cruxitems.api.event.InteractableComponentUseEvent;
import killercreepr.cruxitems.api.item.CruxedItem;
import killercreepr.cruxitems.api.item.component.ConsumableComponent;
import killercreepr.cruxitems.api.item.component.InteractableComponent;
import killercreepr.cruxitems.api.item.consume.ItemConsumeContext;
import killercreepr.cruxitems.api.item.consume.ItemConsumeResult;
import killercreepr.cruxitems.api.item.interaction.InteractableItem;
import killercreepr.cruxitems.api.item.interaction.ItemUseContext;
import killercreepr.cruxitems.api.item.interaction.ItemUseResult;
import killercreepr.cruxitems.api.item.inventory.InventoryItem;
import killercreepr.cruxitems.api.item.inventory.ItemClickContext;
import killercreepr.cruxitems.core.item.consume.ItemConsumeContextImpl;
import killercreepr.cruxitems.core.item.interaction.impl.ItemUseContextImpl;
import killercreepr.cruxitems.core.item.inventory.ItemClickContextImpl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class ItemInteractionListener implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player p)) return;
        ItemStack item = p.getInventory().getItemInMainHand();
        if(CruxItem.isEmpty(item)) return;

        CruxedItem crux = CruxedItem.cruxed(item);
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

        ItemUseResult result = genericUse(ctx);
        if(result != null){
            Boolean cancel = result.getCancelled();
            if(cancel != null) event.setCancelled(cancel);
        }

        if(!(crux.getPluginItem() instanceof InteractableItem pluginItem)) return;

        result = pluginItem.onUse(ctx);
        Boolean cancel = result.getCancelled();
        if(cancel != null) event.setCancelled(cancel);
    }

    public ItemUseResult genericUse(ItemUseContext ctx, Collection<InteractableComponent> list){
        CustomItemPreUseEvent preUseEvent = new CustomItemPreUseEvent(ctx, ItemUseResult.empty());
        if(!preUseEvent.callEvent()) return preUseEvent.getUseResult();

        ItemUseResult defaultResult = preUseEvent.getUseResult();
        for(InteractableComponent c : list){
            if(!c.isUsable(ctx)) continue;
            InteractableComponentUseEvent useEvent = new InteractableComponentUseEvent(ctx, c);
            if(!useEvent.callEvent()) continue;

            ItemUseResult result = c.onUse(ctx);
            if(!result.successful()) continue;
            return result;
        }
        return defaultResult;
    }

    public ItemUseResult genericUse(ItemUseContext ctx){
        Collection<InteractableComponent> list = ctx.getItem().getAllOfTypeOrDefaultData(InteractableComponent.class);
        if(list == null) return null;
        return genericUse(ctx, list);
    }


    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        Entity rightClicked = event.getRightClicked();
        ItemStack item = p.getInventory().getItem(event.getHand());
        if(CruxItem.isEmpty(item)) return;
        CruxedItem crux = CruxedItem.cruxed(item);

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

        ItemUseResult result = genericUse(ctx);
        if(result != null){
            Boolean cancel = result.getCancelled();
            if(cancel != null) event.setCancelled(cancel);
        }

        if(!(crux.getPluginItem() instanceof InteractableItem pluginItem)) return;

        result = pluginItem.onUse(ctx);
        Boolean cancel = result.getCancelled();
        if(cancel != null) event.setCancelled(cancel);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if(CruxItem.isEmpty(item)) return;
        EquipmentSlot hand = event.getHand();
        if(hand==null) return;
        CruxedItem crux = CruxedItem.cruxed(item);
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

        ItemUseResult result = genericUse(ctx);
        if(result != null){
            Boolean cancel = result.getCancelled();
            if(cancel != null) event.setCancelled(cancel);
            Event.Result r = result.getUseInteractedBlock();
            if(r != null) event.setUseInteractedBlock(r);
            r = result.getUseItemInHand();
            if(r != null) event.setUseItemInHand(r);
        }

        if(!(crux.getPluginItem() instanceof InteractableItem pluginItem)) return;

        result = pluginItem.onUse(ctx);
        Boolean cancel = result.getCancelled();
        if(cancel != null) event.setCancelled(cancel);
        Event.Result r = result.getUseInteractedBlock();
        if(r != null) event.setUseInteractedBlock(r);
        r = result.getUseItemInHand();
        if(r != null) event.setUseItemInHand(r);
    }

    protected void clicked(InventoryClickEvent event, CruxedItem crux){
        if(!(crux.getPluginItem() instanceof InventoryItem i)) return;
        ItemClickContext ctx = ItemClickContextImpl.builder()
            .clickedInventory(event.getClickedInventory())
            .clickType(event.getClick())
            .hotbarButton(event.getHotbarButton())
            .slot(event.getSlot())
            .whoClicked(event.getWhoClicked())
            .rawSlot(event.getRawSlot())
            .action(event.getAction())
            .item(crux)
            .build();
        ItemUseResult result = i.onClick(ctx);
        Boolean cancel = result.getCancelled();
        if(cancel != null) event.setCancelled(cancel);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        if(clicked != null){
            clicked(event, CruxedItem.cruxed(clicked));
        }
        int hotbar = event.getHotbarButton();
        if(hotbar != -1){
            clicked = event.getWhoClicked().getInventory().getItem(hotbar);
            if(clicked != null){
                clicked(event, CruxedItem.cruxed(clicked));
            }
        }
    }

    public ItemConsumeResult genericConsume(ItemConsumeContext ctx){
        Collection<ConsumableComponent> list = ctx.getItem().getAllOfTypeOrDefaultData(ConsumableComponent.class);
        if(list == null) return null;
        return genericConsume(ctx, list);
    }

    public ItemConsumeResult genericConsume(ItemConsumeContext ctx, Collection<ConsumableComponent> list){
        CustomItemPreConsumeEvent preUseEvent = new CustomItemPreConsumeEvent(ctx, ItemConsumeResult.empty());
        if(!preUseEvent.callEvent()) return preUseEvent.getUseResult();

        ItemConsumeResult defaultResult = preUseEvent.getUseResult();
        for(ConsumableComponent c : list){
            if(!c.isUsable(ctx)) continue;
            ConsumableComponentUseEvent useEvent = new ConsumableComponentUseEvent(ctx, c);
            if(!useEvent.callEvent()) continue;

            ItemConsumeResult result = c.onUse(ctx);
            if(!result.successful()) continue;
            return result;
        }
        return defaultResult;
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player p = event.getPlayer();
        CruxItem crux = CruxItem.wrap(event.getItem());

        ItemConsumeContext ctx = new ItemConsumeContextImpl(p, crux, event.getReplacement());
        ItemConsumeResult result = genericConsume(ctx);
        if(result==null) return;

        if(result.getCancelled() != null && result.getCancelled()){
            event.setCancelled(true);
        }
        if(result.replaceResultItem()){
            event.setReplacement(result.resultItem());
        }
        if(result.replaceItem()){
            event.setItem(result.item());
        }
    }

}
