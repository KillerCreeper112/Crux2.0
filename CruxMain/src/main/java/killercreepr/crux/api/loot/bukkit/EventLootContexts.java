package killercreepr.crux.api.loot.bukkit;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import io.papermc.paper.event.block.PlayerShearBlockEvent;
import io.papermc.paper.event.player.PlayerTradeEvent;
import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.event.CruxEntityDamageEvent;
import killercreepr.crux.api.event.CruxEntityDeathEvent;
import killercreepr.crux.api.loot.LootContext;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface EventLootContexts {
    static LootContext.Builder builder(){
        return LootContext.builder();
    }
    static LootContext.Builder builder(@NotNull EntityDeathEvent event){
        LivingEntity e = event.getEntity();
        Player killer = e.getKiller();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(e, "victim", "entity")
                    .putAll(killer, "killer", "attacker")
                    .build()
            )
            .location(e.getLocation())
            .looter(killer)
            .looted(e)
            ;
    }
    static LootContext.Builder builder(@NotNull InventoryClickEvent event){
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(event.getCursor(), "cursor")
                    .build()
            )
            .looter(event.getWhoClicked())
            .looted(event.getCurrentItem())
            ;
    }
    static LootContext.Builder builder(@NotNull EntityPlaceEvent event){
        LivingEntity e = event.getPlayer();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(event.getBlock(), "block")
                    .putAll(event.getHand(), "hand")
                    .putAll(event.getBlockFace(), "block_face")
                    .build()
            )
            .location(event.getEntity().getLocation())
            .looter(e)
            .looted(event.getEntity())
            ;
    }

    static LootContext.Builder builder(@NotNull HangingPlaceEvent event){
        LivingEntity e = event.getPlayer();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(event.getBlock(), "block")
                    .putAll(event.getHand(), "hand")
                    .putAll(event.getBlockFace(), "block_face")
                    .putAll(event.getItemStack(), "item")
                    .build()
            )
            .location(event.getEntity().getLocation())
            .looter(e)
            .looted(event.getEntity())
            ;
    }
    static LootContext.Builder builder(@NotNull HangingBreakByEntityEvent event){
        Entity e = event.getRemover();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(event.getCause(), "cause", "remove_cause")
                    .build()
            )
            .location(event.getEntity().getLocation())
            .looter(e)
            .looted(event.getEntity())
            ;
    }
    static LootContext.Builder builder(@NotNull PlayerTradeEvent event){
        LivingEntity e = event.getPlayer();
        var merchant = event.getVillager();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(event.getTrade(), "recipe", "trade")
                    .build()
            )
            .location(merchant.getLocation())
            .looter(e)
            .looted(merchant)
            ;
    }
    static LootContext.Builder builder(@NotNull PlayerAdvancementDoneEvent event){
        LivingEntity e = event.getPlayer();
        return builder()
            .location(e.getLocation())
            .looter(e)
            .looted(event.getAdvancement())
            ;
    }
    static LootContext.Builder builder(@NotNull PlayerArmorChangeEvent event){
        LivingEntity e = event.getPlayer();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(event.getNewItem(), "new_item")
                    .putAll(event.getOldItem(), "old_item")
                    .putAll(event.getSlot(), "slot")
                    .build()
            )
            .location(e.getLocation())
            .looted(event.getNewItem())
            .looter(e)
            ;
    }

    static LootContext.Builder builder(@NotNull CruxEntityDeathEvent event){
        Entity e = event.getEntity();
        Entity killer = event.getDamager();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(e, "victim", "entity")
                    .putAll(killer, "killer", "attacker")
                    .build()
            )
            .location(e.getLocation())
            .looter(killer)
            .looted(e)
            ;
    }

    static LootContext.Builder builder(@NotNull EntityDamageByEntityEvent event){
        Entity victim = event.getEntity();
        Entity attacker = event.getDamager();
        String cause = event.getCause().toString().toLowerCase();
        String type = event.getDamageSource().getDamageType().key().asString();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(victim, "victim", "entity")
                    .putAll(attacker, "attacker")
                    .putAll(cause, "damage_cause")
                    .putAll(type, "damage_type")
                    .putAll(event.getFinalDamage(), "damage")
                    .putAll(event.getDamage(), "raw_damage")
                    .build()
            )
            .location(victim.getLocation())
            .looter(attacker)
            .looted(victim)
            ;
    }

    static LootContext.Builder builder(@NotNull CruxEntityDamageEvent event){
        Entity victim = event.getEntity();
        Entity attacker = event.getDamager();
        String cause = event.getCause() == null ? "null" : event.getCause().toString().toLowerCase();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(victim, "victim", "entity")
                    .putAll(attacker, "attacker")
                    .putAll(cause, "damage_cause")
                    //todo .putAll(type, "damage_type")
                    .putAll(event.getDmg(), "damage")
                    .putAll(event.getTrueDmg(), "raw_damage")
                    .putAll(event.getKb(), "knockback")
                    .putAll(event.getTrueKb(), "raw_knockback")
                    .putAll(event.getUpKb(), "upwards_knockback")
                    .putAll(event.getTrueUpKb(), "raw_upwards_knockback")
                    .build()
            )
            .location(victim.getLocation())
            .looter(attacker)
            .looted(victim)
            ;
    }

    static LootContext.Builder builder(@NotNull EntityDamageEvent event){
        Entity victim = event.getEntity();
        Entity attacker;
        if(event instanceof EntityDamageByEntityEvent e){
            attacker = e.getDamager();
        }else attacker = null;
        String cause = event.getCause().toString().toLowerCase();
        String type = event.getDamageSource().getDamageType().key().asString();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(victim, "victim", "entity", "looted")
                    .putAll(attacker, "attacker", "looter")
                    .putAll(cause, "damage_cause")
                    .putAll(type, "damage_type")
                    .putAll(event.getFinalDamage(), "damage")
                    .putAll(event.getDamage(), "raw_damage")
                    .build()
            )
            .location(victim.getLocation())
            .looter(attacker)
            .looted(victim)
            ;
    }

    static LootContext.Builder builder(@NotNull EntityDamageByBlockEvent event){
        Entity victim = event.getEntity();
        Block attacker = event.getDamager();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(victim, "victim", "entity")
                    .putAll(attacker, "attacker")
                    .build()
            )
            .location(victim.getLocation())
            .looter(attacker)
            .looted(victim)
            ;
    }

    static LootContext.Builder builder(@NotNull BlockBreakEvent event){
        Block block = event.getBlock();
        Entity player = event.getPlayer();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(player, "player", "miner", "entity")
                    .putAll(block, "block", "block_broken")
                    .putAll(event.getExpToDrop(), "exp_to_drop")
                    .build()
            )
            .location(block.getLocation())
            .looter(player)
            .looted(block)
            ;
    }

    static LootContext.Builder builder(@NotNull PlayerHarvestBlockEvent event){
        Block block = event.getHarvestedBlock();
        Entity player = event.getPlayer();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(player, "player", "miner", "entity")
                    .putAll(block, "block", "block_harvested")
                    .putAll(event.getItemsHarvested(), "items", "drops")
                    .putAll(event.getHand(), "hand")
                    .build()
            )
            .location(block.getLocation())
            .looter(player)
            .looted(block)
            ;
    }

    static LootContext.Builder builder(@NotNull BlockDropItemEvent event){
        Block block = event.getBlock();
        Entity player = event.getPlayer();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(player, "player", "miner", "entity")
                    .putAll(block, "block", "block_broken")
                    .putAll(event.getItems(), "items")
                    .putAll(event.getBlockState(), "block_state", "block_state_before")
                    .build()
            )
            .location(block.getLocation())
            .looter(player)
            .looted(block)
            ;
    }

    static LootContext.Builder builder(@NotNull BlockPlaceEvent event){
        Block block = event.getBlock();
        Entity player = event.getPlayer();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(player, "player", "miner", "entity")
                    .putAll(block, "block", "block_placed")
                    .putAll(event.getBlockAgainst(), "block_against")
                    .putAll(event.getBlockReplacedState(), "block_replaced_state")
                    .putAll(event.getHand(), "hand")
                    .build()
            )
            .location(block.getLocation())
            .looter(player)
            .looted(block)
            ;
    }

    static LootContext.Builder builder(@NotNull PlayerFishEvent event){
        Entity caught = event.getCaught();
        Player player = event.getPlayer();
        FishHook hook = event.getHook();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(event.getState(), "state")
                    .putAll(player, "player")
                    .putAll(event.getExpToDrop(), "exp", "exp_to_drop")
                    .putAll(hook, "hook", "fish_hook")
                    .putAll(event.getHand(), "hand")
                    .putAll(caught, "caught")
                    .build()
            )
            .location(event.getHook().getLocation())
            .looter(player)
            .looted(caught)
            ;
    }

    static LootContext.Builder builder(@NotNull PlayerItemConsumeEvent event){
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        ItemStack replacement = event.getReplacement();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(player, "player")
                    .putAll(item, "consumable", "consumed", "item")
                    .putAll(replacement, "replacement")
                    .putAll(event.getHand(), "hand")
                    .build()
            )
            .location(player.getLocation())
            .looter(player)
            .looted(item)
            ;
    }

    static LootContext.Builder builder(@NotNull PlayerChangedWorldEvent event){
        Player player = event.getPlayer();
        World world = event.getFrom();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(player, "player")
                    .putAll(player.getWorld(), "world", "to")
                    .putAll(world, "from")
                    .build()
            )
            .location(player.getLocation())
            .looter(player)
            .looted(player.getWorld())
            ;
    }

    static LootContext.Builder builder(@NotNull EntityBreedEvent event){
        LivingEntity breeder = event.getBreeder();
        Entity entity = event.getEntity();
        Entity mother = event.getMother();
        Entity father = event.getFather();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(breeder, "breeder")
                    .putAll(entity, "entity", "child")
                    .putAll(mother, "mother")
                    .putAll(father, "father")
                    .putAll(event.getExperience(), "exp_to_drop", "exp")
                    .putAll(event.getBredWith(), "bred_with", "item_used", "used_item")
                    .build()
            )
            .location(entity.getLocation())
            .looter(breeder)
            .looted(mother)
            ;
    }

    static LootContext.Builder builder(@NotNull PlayerShearEntityEvent event){
        Player p = event.getPlayer();
        Entity entity = event.getEntity();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(p, "player")
                    .putAll(entity, "entity", "sheared")
                    .putAll(event.getItem(), "item", "item_used", "used_item")
                    .putAll(event.getDrops(), "drops")
                    .putAll(event.getHand(), "hand")
                    .build()
            )
            .location(entity.getLocation())
            .looter(p)
            .looted(entity)
            ;
    }

    static LootContext.Builder builder(@NotNull PlayerShearBlockEvent event){
        Player p = event.getPlayer();
        Block block = event.getBlock();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(p, "player")
                    .putAll(block, "block", "sheared")
                    .putAll(event.getItem(), "item", "item_used", "used_item")
                    .putAll(event.getDrops(), "drops")
                    .putAll(event.getHand(), "hand")
                    .build()
            )
            .location(block.getLocation())
            .looter(p)
            .looted(block)
            ;
    }

    static LootContext.Builder builder(@NotNull EntityResurrectEvent event){
        LivingEntity p = event.getEntity();
        ItemStack looted;
        EntityEquipment equipment = p.getEquipment();
        if(equipment != null && event.getHand() != null){
            looted = equipment.getItem(event.getHand());
        }else looted = null;
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(event.getHand(), "hand")
                    .putAll(looted, "item_used", "used_item")
                    .build()
            )
            .location(p.getLocation())
            .looter(p)
            .looted(looted)
            ;
    }

    static LootContext.Builder builder(@NotNull EntityTameEvent event){
        LivingEntity tamed = event.getEntity();
        AnimalTamer tamer = event.getOwner();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(tamer, "owner", "tamer")
                    .putAll(tamed, "entity", "tamed")
                    .build()
            )
            .location(tamed.getLocation())
            .looter(tamer)
            .looted(tamed)
            ;
    }

    static LootContext.Builder builder(@NotNull EntityPickupItemEvent event){
        LivingEntity e = event.getEntity();
        Item item = event.getItem();
        return builder()
            .info(
                DataExchange.builder()
                    .putAll(e, "entity")
                    .putAll(item, "item", "picked_up")
                    .putAll(event.getRemaining(), "remaining", "remaining_amount")
                    .build()
            )
            .location(item.getLocation())
            .looter(e)
            .looted(item)
            ;
    }
}
