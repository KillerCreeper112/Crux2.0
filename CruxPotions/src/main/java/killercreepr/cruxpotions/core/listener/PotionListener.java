package killercreepr.cruxpotions.core.listener;

import com.destroystokyo.paper.event.entity.WitchConsumePotionEvent;
import killercreepr.crux.api.entity.CruxEntity;
import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.cruxpotions.api.entity.PotionHolder;
import killercreepr.cruxpotions.api.potion.inflictor.PotionInflictor;
import killercreepr.cruxpotions.api.values.ValuesProvider;
import killercreepr.cruxpotions.core.CruxPotions;
import killercreepr.cruxpotions.core.component.PotionComponents;
import killercreepr.cruxpotions.core.entity.memory.SimplePotionHolder;
import killercreepr.cruxpotions.api.potion.StoredPotion;
import killercreepr.cruxpotions.core.potions.inflictor.EntityInflictor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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
import java.util.List;

public class PotionListener implements Listener {
    protected final Plugin plugin;
    protected final ValuesProvider cfg;
    public PotionListener(@NotNull Plugin plugin, @NotNull ValuesProvider cfg) {
        this.plugin = plugin;
        this.cfg = cfg;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        PotionHolder data = EntityMemory.getDataHolder(event.getEntity(), SimplePotionHolder.class);
        if(data != null && cfg.removePotionsUponDeath().value()){
            data.clearPotions();
        }
    }

    public void applyPotion(@NotNull Entity p, @Nullable ItemStack item){
        if(item==null) return;
        if(!CruxPotions.canApplyPotion(p)) return;
        PotionHolder data = EntityMemory.getOrCreateDataHolder(p, SimplePotionHolder.class);
        if(data==null) return;
        Collection<StoredPotion> potions = CruxItem.wrap(item).get(PotionComponents.STORED_CRUX_POTIONS);
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
        Collection<StoredPotion> potions = CruxItem.wrap(item).get(PotionComponents.STORED_CRUX_POTIONS);
        if(potions == null || potions.isEmpty()) return;
        PotionInflictor inflictor = new EntityInflictor(potion);
        for(LivingEntity e : event.getAffectedEntities()){
            SimplePotionHolder data = EntityMemory.getOrCreateDataHolder(e, SimplePotionHolder.class);
            if(data==null) continue;
            double intensity = event.getIntensity(e);
            for(StoredPotion h : potions){
                int duration = (int) (intensity * (double) h.getDuration() + 0.5D);
                data.addPotion(h.withDuration(duration).create(e, inflictor));
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onLingeringPotionSplash(LingeringPotionSplashEvent event) {
        List<StoredPotion> potions = CruxItem.wrap(event.getEntity().getItem()).get(PotionComponents.STORED_CRUX_POTIONS);
        if(potions == null || potions.isEmpty()) return;
        Entity cloud = event.getAreaEffectCloud();
        CruxEntity.entity(cloud).set(PotionComponents.STORED_CRUX_POTIONS, potions);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onAreaEffectCloudApply(AreaEffectCloudApplyEvent event) {
        Entity cloud = event.getEntity();
        Collection<StoredPotion> potions = CruxEntity.entity(cloud).get(PotionComponents.STORED_CRUX_POTIONS);
        if(potions == null || potions.isEmpty()) return;
        PotionInflictor inflictor = new EntityInflictor(cloud);
        for(Entity e : event.getAffectedEntities()){
            SimplePotionHolder data = EntityMemory.getOrCreateDataHolder(e, SimplePotionHolder.class);
            if(data==null) continue;
            for(StoredPotion h : potions){
                data.addPotion(h.create(e, inflictor));
            }
        }
    }
}
