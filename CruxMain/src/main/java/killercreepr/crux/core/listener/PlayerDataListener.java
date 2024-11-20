package killercreepr.crux.core.listener;

import killercreepr.crux.api.entity.memory.PlayerMemory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDataListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        PlayerMemory data = PlayerMemory.get(p);
        if(data==null) return;
        data.quit(System.currentTimeMillis() + 50L);
    }
}
