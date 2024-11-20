package killercreepr.cruxpotions.listener;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.entity.memory.PlayerMemory;
import killercreepr.cruxpotions.config.PlayerDataFile;
import killercreepr.cruxpotions.data.PotionHolder;
import killercreepr.cruxpotions.values.ValuesProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

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
        if(memory.getDataHolder(PotionHolder.KEY) instanceof PotionHolder data){
            data.setPotions(new PlayerDataFile(plugin, p.getUniqueId()).getPotions());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(!cfg.savePotionsUponQuit().value()) return;
        Player p = event.getPlayer();
        PotionHolder data = EntityMemory.getDataHolder(p, PotionHolder.class);
        if(data != null){
            data.stopPotions();
            new PlayerDataFile(plugin, p.getUniqueId()).savePotions(data.getActiveEffects()).save();
        }
    }
}
