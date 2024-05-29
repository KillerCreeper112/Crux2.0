package killercreepr.cruxpotion.listener;

import com.destroystokyo.paper.event.entity.WitchConsumePotionEvent;
import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.data.entity.PlayerMemory;
import killercreepr.cruxpotion.PotionCore;
import killercreepr.cruxpotion.config.Config;
import killercreepr.cruxpotion.config.PlayerConfig;
import killercreepr.cruxpotion.data.PotionHolder;
import killercreepr.cruxpotion.persistence.CustomPotionHolder;
import killercreepr.cruxpotion.persistence.PotionPersistTags;
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
    protected final Config cfg;
    public PotionListener(@NotNull Plugin plugin, @NotNull Config cfg) {
        this.plugin = plugin;
        this.cfg = cfg;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        PotionHolder data = EntityMemory.getDataHolder(event.getEntity(), PotionHolder.class);
        if(data != null && cfg.REMOVE_POTIONS_UPON_DEATH.value()){
            data.clearPotions();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!cfg.SAVE_POTIONS_UPON_QUIT.value()) return;
        Player p = event.getPlayer();
        PlayerMemory memory = PlayerMemory.getOrCreate(p);
        if(memory.getHolder(PotionHolder.KEY) instanceof PotionHolder data){
            data.setPotions(new PlayerConfig(plugin, p.getUniqueId()).getPotions());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(!cfg.SAVE_POTIONS_UPON_QUIT.value()) return;
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
        if(!PotionCore.canApplyPotion(p)) return;
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
        if(!PotionCore.canApplyPotion(p)) return;
        EntityMemory memory = EntityMemory.getOrCreate(p);
        if(!(memory.getHolder(PotionHolder.KEY) instanceof PotionHolder data)) return;
        Collection<CustomPotionHolder> potions = PotionPersistTags.STORED_CUSTOM_POTIONS.get(event.getPotion(),null);
        if(potions == null || potions.isEmpty()) return;
        for(CustomPotionHolder h : potions){
            data.addPotion(h.create(p));
        }
    }
}
