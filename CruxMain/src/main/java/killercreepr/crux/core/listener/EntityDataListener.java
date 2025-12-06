package killercreepr.crux.core.listener;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import killercreepr.crux.api.communication.Communicator;
import killercreepr.crux.api.communication.CreateTitle;
import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.entity.memory.PlayerMemory;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.communication.animation.AnimatedMsg;
import killercreepr.crux.core.communication.animation.LetterCycleTextAnimation;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EntityDataListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        PlayerMemory mem = PlayerMemory.get(uuid);
        if(mem != null){
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.kickMessage(Component.text("Please wait before joining! Code: 101"));
            return;
        }
        var future = EntityMemory.REMOVING_FUTURES.get(uuid);
        if(future == null) return;
        if(future.isDone() || future.isCancelled()){
            EntityMemory.REMOVING_FUTURES.remove(uuid);
            return;
        }
        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        event.kickMessage(Component.text("Please wait before joining! Code: 102"));
    }

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
        if(e instanceof HumanEntity) return;
        EntityMemory mem = EntityMemory.get(e);
        if(mem == null) return;
        try{
            mem.onMemoryUnload(e);
        }catch (Exception ignored){
            ignored.printStackTrace();
        }
    }

    //todo remove testing
    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player p = event.getPlayer();
        AnimatedMsg msg = new AnimatedMsg(
            List.of(
                new AnimatedMsg.Entry(
                    Communicator.title(CreateTitle.title(
                        "<text_animation_output:test>",
                        "",
                        0, 30, 0
                    )),
                    20
                )
            ),
            Map.of("test", new LetterCycleTextAnimation(
                "This is a text",
                "<bold><green><output>",
                List.of("<yellow><char></yellow>"),
                2, 20,
                true
            )),
            NumberProvider.equation("<max_animation_length>")
        );
        msg.use(p, TagContainer.merged().hook(p));
    }

}
