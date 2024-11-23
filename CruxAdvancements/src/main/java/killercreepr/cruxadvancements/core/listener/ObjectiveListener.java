package killercreepr.cruxadvancements.core.listener;

import io.papermc.paper.event.block.PlayerShearBlockEvent;
import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.cruxadvancements.api.event.PlayerCraftItemEvent;
import killercreepr.cruxadvancements.core.advancement.objective.standard.*;
import killercreepr.cruxadvancements.core.entity.memory.AdvancementHolder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.jetbrains.annotations.NotNull;

public class ObjectiveListener implements Listener {
    /*todo when Paper PR merges https://github.com/PaperMC/Paper/pull/5736 @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPiglinBarter(PiglinBarterEvent event) {
    }*/
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if(!(event.getEntity() instanceof Player p)) return;
        Item item = event.getItem();
        if(p.getUniqueId().equals(item.getThrower())) return;

        AdvancementHolder holder = holder(p);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(PickupItemObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityBreed(EntityBreedEvent event) {
        if(!(event.getBreeder() instanceof Player p)) return;
        AdvancementHolder holder = holder(p);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(BreedEntityObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {
        Player p = event.getPlayer();
        AdvancementHolder holder = holder(p);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(ShearEntityObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerShearBlock(PlayerShearBlockEvent event) {
        Player p = event.getPlayer();
        AdvancementHolder holder = holder(p);
        if(holder==null) return;

        holder.getAdvancementTracker().apply(ShearBlockObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockDropItem(BlockDropItemEvent event) {
        if(event.getItems().isEmpty()) return;
        Player p = event.getPlayer();
        AdvancementHolder holder = holder(p);
        if(holder==null) return;

        holder.getAdvancementTracker().apply(BreakBlockDropObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityResurrect(EntityResurrectEvent event) {
        if(!(event.getEntity() instanceof Player p)) return;
        AdvancementHolder holder = holder(p);
        if(holder==null) return;

        holder.getAdvancementTracker().apply(ResurrectObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityTame(EntityTameEvent event) {
        if(!(event.getEntity() instanceof Player p)) return;
        AdvancementHolder holder = holder(p);
        if(holder==null) return;

        holder.getAdvancementTracker().apply(TameEntityObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }


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

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity victim = event.getEntity();
        Player killer = victim.getKiller();
        if(killer == null) return;
        AdvancementHolder holder = holder(killer);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(KillEntityObjective.class, (manager, advancement, objective) -> {
            objective.trigger(killer.getUniqueId(), manager, advancement, event);
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

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerCraftItem(PlayerCraftItemEvent event) {
        if(!(event.getWhoClicked() instanceof Player p)) return;
        AdvancementHolder holder = holder(p);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(CraftItemObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }

}
