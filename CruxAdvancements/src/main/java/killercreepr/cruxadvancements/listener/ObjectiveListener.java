package killercreepr.cruxadvancements.listener;

import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.cruxadvancements.advancement.objective.impl.*;
import killercreepr.cruxadvancements.data.entity.AdvancementHolder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.jetbrains.annotations.NotNull;

public class ObjectiveListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        AdvancementHolder holder = holder(p);
        if(holder==null) return;

        holder.getAdvancementTracker().apply(BreakBlockObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player p = event.getPlayer();
        AdvancementHolder holder = holder(p);
        if(holder==null) return;

        holder.getAdvancementTracker().apply(PlaceBlockObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerFish(PlayerFishEvent event) {
        Player p = event.getPlayer();
        AdvancementHolder holder = holder(p);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(FishObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });

        PlayerFishEvent.State state = event.getState();
        if(state == PlayerFishEvent.State.CAUGHT_FISH){
            Entity caught = event.getCaught();
            if(caught != null){
                holder.getAdvancementTracker().apply(CatchFishObjective.class, (manager, advancement, objective) -> {
                    objective.trigger(p.getUniqueId(), manager, advancement, event);
                });
            }
        }
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof LivingEntity victim)) return;
        if(!(event.getDamager() instanceof Player dmger)) return;
        AdvancementHolder holder = holder(dmger);
        if(holder==null) return;

        holder.getAdvancementTracker().apply(KillEntityObjective.class, (manager, advancement, objective) -> {
            objective.trigger(dmger.getUniqueId(), manager, advancement, event);
        });
    }

    private AdvancementHolder holder(@NotNull Player p){
        return EntityMemory.getOrCreateDataHolder(p, AdvancementHolder.class);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player p = event.getPlayer();
        AdvancementHolder holder = holder(p);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(ConsumeItemObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player p = event.getPlayer();
        AdvancementHolder holder = holder(p);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(TravelToWorldObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }
}
