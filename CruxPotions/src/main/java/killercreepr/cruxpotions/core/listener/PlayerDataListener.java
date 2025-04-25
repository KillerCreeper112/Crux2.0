package killercreepr.cruxpotions.core.listener;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.entity.memory.PlayerMemory;
import killercreepr.cruxpotions.api.potion.StoredPotion;
import killercreepr.cruxpotions.api.values.ValuesProvider;
import killercreepr.cruxpotions.core.config.PlayerDataFile;
import killercreepr.cruxpotions.core.entity.memory.SimplePotionHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayerDataListener implements Listener {
    protected final Plugin plugin;
    protected final ValuesProvider cfg;
    public PlayerDataListener(@NotNull Plugin plugin, @NotNull ValuesProvider cfg) {
        this.plugin = plugin;
        this.cfg = cfg;
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!cfg.savePotionsUponQuit().value()) return;
        Player p = event.getPlayer();
        PlayerMemory memory = PlayerMemory.getOrCreate(p);
        if(memory.getDataHolder(SimplePotionHolder.KEY) instanceof SimplePotionHolder data){
            var file = new PlayerDataFile(plugin, "data/cruxpotions/player/" + p.getUniqueId());
            file.reloadIfNeeded();
            file.register();
            var stored = file.CRUX_POTIONS.value();
            file.close();
            if(stored == null || stored.isEmpty()) return;
            stored.forEach(st ->{
                data.addPotion(st.create(p));
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(!cfg.savePotionsUponQuit().value()) return;
        Player p = event.getPlayer();
        SimplePotionHolder data = EntityMemory.getDataHolder(p, SimplePotionHolder.class);
        if(data != null){
            data.stopPotions();
            var file = new PlayerDataFile(plugin, "data/cruxpotions/player/" + p.getUniqueId());
            if(data.getActiveEffects().isEmpty()){
                file.CRUX_POTIONS.setValue(null);
            }else{
                List<StoredPotion> stored = new ArrayList<>();
                data.getActiveEffects().forEach(active ->{
                    stored.add(active.store());
                });
                file.CRUX_POTIONS.setValue(stored);
            }
            if(file.setupContents(true, true)) file.save();
            //new PlayerDataFile(plugin, p.getUniqueId()).savePotions(data.getActiveEffects()).save();
        }
    }
}
