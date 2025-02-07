package killercreepr.cruxtickables.core.listener;

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.core.Crux;
import killercreepr.cruxattributes.api.equipment.CruxSlot;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickable;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickableInstance;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickableModifier;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickablesContainer;
import killercreepr.cruxtickables.core.component.CruxTickableComponents;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TickableListener implements Listener {
    /**
     * @param mainSlot The main slot that the attributes are coming from.
     */
    private void applyModifiersFromItem(@NotNull Player p, @NotNull ItemStack i, @NotNull CruxSlot mainSlot){
        CruxItem crux = CruxItem.wrap(i);
        EntityTickablesContainer container = crux.get(CruxTickableComponents.ENTITY_TICKABLES);
        if(container == null) return;

        for(EntityTickableInstance a : container.getTickableInstances()){
            for(EntityTickableModifier m : a.getModifiers()){
                if(!m.getSlotGroup().test(mainSlot)) continue;
                EntityTickable.addModifier(p.getPersistentDataContainer(), a.getTickable(), m,
                    mainSlot.key(), m.getSlotGroup().key(), Crux.key(UUID.randomUUID().toString()));
            }
        }
    }

    private void updateSlot(@NotNull Player p, @Nullable ItemStack i, @NotNull CruxSlot slot){
        EntityTickable.removeModifiers(p, slot.key());
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
}
