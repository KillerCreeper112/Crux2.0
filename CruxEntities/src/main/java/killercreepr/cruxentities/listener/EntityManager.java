package killercreepr.cruxentities.listener;

import killercreepr.cruxentities.entity.CruxMob;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class EntityManager implements Listener {
    protected final Plugin plugin;

    public EntityManager(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntitiesLoad(EntitiesLoadEvent event) {
        plugin.getServer().getScheduler().runTask(plugin, task ->{
            for(Entity e : event.getEntities()){
                if(!e.isValid()) continue;
                loadEntity(e);
            }
        });
    }

    public static void loadEntity(@NotNull Entity e){
        if(e.getType() == EntityType.UNKNOWN) return;
        CruxMob mob = CruxMob.get(e);
        if(mob==null) return;
        mob.load(e);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        Entity e = event.getEntity();
        CruxMob mob = CruxMob.get(e);
        if(mob==null) return;
        mob.onDeath(e, event);
    }


    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
    }

}
