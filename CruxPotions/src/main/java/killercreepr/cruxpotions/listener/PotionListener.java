package killercreepr.cruxpotions.listener;

import com.destroystokyo.paper.event.entity.WitchConsumePotionEvent;
import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.data.entity.PlayerMemory;
import killercreepr.cruxpotions.CruxPotions;
import killercreepr.cruxpotions.config.PlayerConfig;
import killercreepr.cruxpotions.data.PotionHolder;
import killercreepr.cruxpotions.persistence.CustomPotionHolder;
import killercreepr.cruxpotions.persistence.PotionPersistTags;
import killercreepr.cruxpotions.values.ValuesProvider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!cfg.savePotionsUponQuit().value()) return;
        Player p = event.getPlayer();
        PlayerMemory memory = PlayerMemory.getOrCreate(p);
        if(memory.getHolder(PotionHolder.KEY) instanceof PotionHolder data){
            data.setPotions(new PlayerConfig(plugin, p.getUniqueId()).getPotions());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(!cfg.savePotionsUponQuit().value()) return;
        Player p = event.getPlayer();
        PotionHolder data = PlayerMemory.getDataHolder(p, PotionHolder.class);
        if(data != null){
            data.stopPotions();
            new PlayerConfig(plugin, p.getUniqueId()).savePotions(data.getActiveEffects()).save();
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player p = event.getPlayer();
        if(!CruxPotions.canApplyPotion(p)) return;
        PlayerMemory memory = PlayerMemory.get(p);
        if(memory == null || !(memory.getHolder(PotionHolder.KEY) instanceof PotionHolder data)) return;
        Collection<CustomPotionHolder> potions = PotionPersistTags.STORED_CUSTOM_POTIONS.get(event.getItem(), null);
        if(potions == null || potions.isEmpty()) return;
        for(CustomPotionHolder h : potions){
            data.addPotion(h.create(p));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onWitchConsumePotion(WitchConsumePotionEvent event) {
        Entity p = event.getEntity();
        if(!CruxPotions.canApplyPotion(p)) return;
        EntityMemory memory = EntityMemory.getOrCreate(p);
        if(!(memory.getHolder(PotionHolder.KEY) instanceof PotionHolder data)) return;
        Collection<CustomPotionHolder> potions = PotionPersistTags.STORED_CUSTOM_POTIONS.get(event.getPotion(),null);
        if(potions == null || potions.isEmpty()) return;
        for(CustomPotionHolder h : potions){
            data.addPotion(h.create(p));
        }
    }
}
