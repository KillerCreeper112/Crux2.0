package killercreepr.cruxpotions.listener;

import com.destroystokyo.paper.event.entity.WitchConsumePotionEvent;
import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.cruxpotions.CruxPotions;
import killercreepr.cruxpotions.data.PotionHolder;
import killercreepr.cruxpotions.persistence.PotionPersistTags;
import killercreepr.cruxpotions.persistence.StoredPotion;
import killercreepr.cruxpotions.potions.inflictor.EntityInflictor;
import killercreepr.cruxpotions.potions.inflictor.PotionInflictor;
import killercreepr.cruxpotions.values.ValuesProvider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class PotionListener implements Listener {
    protected final Plugin plugin;
    protected final ValuesProvider cfg;
    public PotionListener(@NotNull Plugin plugin, @NotNull ValuesProvider cfg) {
        this.plugin = plugin;
        this.cfg = cfg;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        PotionHolder data = EntityMemory.getDataHolder(event.getEntity(), PotionHolder.class);
        if(data != null && cfg.removePotionsUponDeath().value()){
            data.clearPotions();
        }
    }

    public void applyPotion(@NotNull Entity p, @Nullable ItemStack item){
        if(item==null) return;
        if(!CruxPotions.canApplyPotion(p)) return;
        PotionHolder data = EntityMemory.getOrCreateDataHolder(p, PotionHolder.class);
        if(data==null) return;
        Collection<StoredPotion> potions = PotionPersistTags.STORED_CUSTOM_POTIONS.get(item, null);
        if(potions == null || potions.isEmpty()) return;
        PotionInflictor inflictor = new EntityInflictor(p);
        for(StoredPotion h : potions){
            data.addPotion(h.create(p, inflictor));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player p = event.getPlayer();
        applyPotion(p, event.getItem());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onWitchConsumePotion(WitchConsumePotionEvent event) {
        Entity p = event.getEntity();
        applyPotion(p, event.getPotion());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPotionSplash(PotionSplashEvent event) {
        if(event.getAffectedEntities().isEmpty()) return;
        ThrownPotion potion = event.getPotion();
        ItemStack item = potion.getItem();
        Collection<StoredPotion> potions = PotionPersistTags.STORED_CUSTOM_POTIONS.get(item, null);
        if(potions == null || potions.isEmpty()) return;
        PotionInflictor inflictor = new EntityInflictor(potion);
        for(Entity e : event.getAffectedEntities()){
            PotionHolder data = EntityMemory.getOrCreateDataHolder(e, PotionHolder.class);
            if(data==null) continue;
            for(StoredPotion h : potions){
                data.addPotion(h.create(e, inflictor));
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onLingeringPotionSplash(LingeringPotionSplashEvent event) {
        Collection<StoredPotion> potions = PotionPersistTags.STORED_CUSTOM_POTIONS.get(event.getEntity().getItem(), null);
        if(potions == null || potions.isEmpty()) return;
        Entity cloud = event.getAreaEffectCloud();
        PotionPersistTags.STORED_CUSTOM_POTIONS.set(cloud, potions);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onAreaEffectCloudApply(AreaEffectCloudApplyEvent event) {
        Entity cloud = event.getEntity();
        Collection<StoredPotion> potions = PotionPersistTags.STORED_CUSTOM_POTIONS.get(cloud, null);
        if(potions == null || potions.isEmpty()) return;
        PotionInflictor inflictor = new EntityInflictor(cloud);
        for(Entity e : event.getAffectedEntities()){
            PotionHolder data = EntityMemory.getOrCreateDataHolder(e, PotionHolder.class);
            if(data==null) continue;
            for(StoredPotion h : potions){
                data.addPotion(h.create(e, inflictor));
            }
        }
    }
}
