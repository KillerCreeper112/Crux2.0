package killercreepr.cruxattributes.core.listener;

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import killercreepr.crux.Crux;
import killercreepr.crux.util.CruxItem;
import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxSlot;
import killercreepr.cruxattributes.core.attribute.CruxAttributeInstance;
import killercreepr.cruxattributes.core.attribute.CruxAttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CruxAttributeListener implements Listener {
    /**
     * //todo change this
     * An example of this is would be:
     * mainSlot = HAND, subSlot = null (ALL)
     * OR accessories can be activated from ANY_ACCESSORY or ALL, like this:
     * mainSlot = ACCESSORY_8, subSlot = null (ALL)
     * mainSlot = ACCESSORY_8, subSlot = ANY_ACCESSORY
     * @param mainSlot The main slot that the attributes are coming from.
     * @param subSlot The slot that can activate the main slot. A null value is ALL.
     */
    private void applyModifiersFromItem(@NotNull Player p, @NotNull ItemStack i, @NotNull CruxSlot mainSlot, @Nullable CruxSlot subSlot){
        for(CruxAttributeInstance a : CruxAttribute.getInstances(i, subSlot)){
            for(CruxAttributeModifier m : a.getModifiers()){
                CruxAttribute.addModifier(p, a.getAttribute(), m/*.withKey(Crux.key(UUID.randomUUID().toString()))*/,
                        mainSlot.key(), subSlot == null ? Crux.key("all") : subSlot.key(), Crux.key(UUID.randomUUID().toString()));
            }
        }
    }

    private void updateSlot(@NotNull Player p, @Nullable ItemStack i, @NotNull CruxSlot slot){
        CruxAttribute.removeModifiers(p, slot.key());
        if(!CruxItem.isEmpty(i)){
            applyModifiersFromItem(p, i, slot, null);
            /*for(CruxSlot sub : slot.activationSlots()){
                applyModifiersFromItem(p, i, slot, sub);
            }*/
        }

        /*new BukkitRunnable(){
            @Override
            public void run() {
                PlayerMemory.get(p).updateTickables();
            }
        }.runTaskLater(DP.inst(), 1L);*/
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player p = event.getPlayer();
        ItemStack i = p.getInventory().getItem(event.getNewSlot());
        updateSlot(p, i, CruxSlot.HAND);
    }

    @EventHandler
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
