package killercreepr.cruxadvancements.core.listener;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import io.papermc.paper.event.block.PlayerShearBlockEvent;
import io.papermc.paper.event.player.PlayerTradeEvent;
import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.event.CruxEntityDeathEvent;
import killercreepr.crux.core.Crux;
import killercreepr.cruxadvancements.api.event.CruxAdvancementGrantEvent;
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
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ObjectiveListener implements Listener {
    /*todo when Paper PR merges https://github.com/PaperMC/Paper/pull/5736 @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPiglinBarter(PiglinBarterEvent event) {
    }*/
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        Item item = event.getItem();
        UUID thrower = item.getThrower();
        if(thrower != null){
            Player p = Crux.getServer().getPlayer(thrower);
            if(p != null){
                AdvancementHolder holder = holder(p);
                if(holder!=null){
                    holder.getAdvancementTracker().apply(EntityPickupItemObjective.class, (manager, advancement, objective) -> {
                        objective.trigger(p.getUniqueId(), manager, advancement, event);
                    });
                }
            }
        }

        if(!(event.getEntity() instanceof Player p)) return;
        if(p.getUniqueId().equals(thrower)) return;

        AdvancementHolder holder = holder(p);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(PickupItemObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event) {
        Player p = event.getPlayer();
        AdvancementHolder holder = holder(p);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(CompleteAdvancementObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTrade(PlayerTradeEvent event) {
        Player p = event.getPlayer();
        AdvancementHolder holder = holder(p);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(TradeObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCruxAdvancementGrant(CruxAdvancementGrantEvent event) {
        Player p = Crux.getServer().getPlayer(event.getWho());
        if(p == null) return;
        AdvancementHolder holder = holder(p);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(CruxAdvancementCompleteObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerHarvestBlock(PlayerHarvestBlockEvent event) {
        Player p = event.getPlayer();
        AdvancementHolder holder = holder(p);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(HarvestBlockObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerArmorChange(PlayerArmorChangeEvent event) {
        Player p = event.getPlayer();
        AdvancementHolder holder = holder(p);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(ArmorChangeObjective.class, (manager, advancement, objective) -> {
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
    public void onHangingPlace(HangingPlaceEvent event) {
        Player p = event.getPlayer();
        if(p == null) return;
        AdvancementHolder holder = holder(p);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(HangingPlaceObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        if(!(event.getRemover() instanceof Player p)) return;
        AdvancementHolder holder = holder(p);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(HangingBreakObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityPlace(EntityPlaceEvent event) {
        Player p = event.getPlayer();
        if(p==null) return;
        AdvancementHolder holder = holder(p);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(PlaceEntityObjective.class, (manager, advancement, objective) -> {
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
    public void onEntityDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player e)) return;
        AdvancementHolder holder = holder(e);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(TakeDamageObjective.class, (manager, advancement, objective) -> {
            objective.trigger(e.getUniqueId(), manager, advancement, event);
        });
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCruxEntityDeath(CruxEntityDeathEvent event) {
        if(!(event.getDamager() instanceof Player killer)) return;
        AdvancementHolder holder = holder(killer);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(KillEntityObjective.class, (manager, advancement, objective) -> {
            objective.trigger(killer.getUniqueId(), manager, advancement, event);
        });
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
