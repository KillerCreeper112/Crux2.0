package killercreepr.crux.core.listener;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.entity.memory.PlayerMemory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.CompletableFuture;

public class EntityDataListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        PlayerMemory data = PlayerMemory.get(p);
        if(data==null) return;
        try{
            data.scheduleForRemoval(p);
        }catch (Exception ignored){
            ignored.printStackTrace();
        }

        try{
            data.onMemoryUnload(p);
        }catch (Exception ignored){
            ignored.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityRemoveFromWorld(EntityRemoveFromWorldEvent event) {
        Entity e = event.getEntity();
        EntityMemory mem = EntityMemory.get(e);
        if(mem == null) return;
        try{
            mem.onMemoryUnload(e);
        }catch (Exception ignored){
            ignored.printStackTrace();
        }
    }
}
