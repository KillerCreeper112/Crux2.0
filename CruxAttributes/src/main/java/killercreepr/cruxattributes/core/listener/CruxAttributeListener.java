package killercreepr.cruxattributes.core.listener;

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import killercreepr.crux.api.entity.CruxEntity;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.core.Crux;
import killercreepr.cruxattributes.api.attribute.*;
import killercreepr.cruxattributes.api.equipment.CruxSlot;
import killercreepr.cruxattributes.core.component.CruxAttributeComponents;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public class CruxAttributeListener implements Listener {
    /**
     * @param mainSlot The main slot that the attributes are coming from.
     */
    private void applyModifiersFromItem(@NotNull Player p, @NotNull ItemStack i, @NotNull CruxSlot mainSlot){
        CruxItem crux = CruxItem.wrap(i);
        CruxAttributeContainer container = crux.getOrDefaultData(CruxAttributeComponents.CRUX_ATTRIBUTES);
        if(container == null) return;

        for(CruxAttributeInstance a : container.getAttributeInstances()){
            for(CruxAttributeModifier m : a.getModifiers()){
                if(!m.getSlotGroup().test(mainSlot)) continue;
                CruxAttribute.addModifier(p, a.getAttribute(), m,
                    mainSlot.key(), m.getSlotGroup().key(), Crux.key(UUID.randomUUID().toString()));
            }
        }
    }

    private void updateSlot(@NotNull Player p, @Nullable ItemStack i, @NotNull CruxSlot slot){
        CruxAttribute.removeModifiers(p, slot.key());
        if(!CruxItem.isEmpty(i)){
            applyModifiersFromItem(p, i, slot);
        }

        /*new BukkitRunnable(){
            @Override
            public void run() {
                PlayerMemory.get(p).updateTickables();
            }
        }.runTaskLater(DP.inst(), 1L);*/
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player p = event.getPlayer();
        ItemStack i = p.getInventory().getItem(event.getNewSlot());
        updateSlot(p, i, CruxSlot.MAIN_HAND);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerInventorySlotChange(PlayerInventorySlotChangeEvent event){
        Player p = event.getPlayer();
        CruxSlot slot = CruxSlot.getActivationSlot(p, event.getSlot());
        if(slot == null) return;
        updateSlot(p, event.getNewItemStack(), slot);
        /*if(slot == CruxSlot.HEAD){
            PlayerMemory data = PlayerMemory.get(p);
            CosmeticHelmet cosmetic = data.getCosmeticHolder().getHelmet();
            if(cosmetic != null) cosmetic.update(event.getNewItemStack());
        }*/
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntitiesUnload(EntitiesUnloadEvent event) {
        var handler = CruxAttributeCacheHandler.attributeCacheHandler();
        event.getEntities().forEach(e ->{
            var cache =  handler.removeCache(e);
            if(cache == null) return;
            saveAttributes(e, cache);
        });
    }

    public void saveAttributes(Entity e, CruxAttributeHandler handler){
        CruxEntity.entity(e).set(CruxAttributeComponents.CRUX_ATTRIBUTES,
            CruxAttributeContainer.container((Collection<CruxAttributeInstance>) handler.getInstances()));
    }
}
