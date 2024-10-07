package killercreepr.crux.loot;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.loot.impl.SimpleLootContext;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public interface LootContext {
    static Builder builder(){
        return new SimpleLootContext.Builder();
    }

    static Builder builder(@NotNull EntityDeathEvent event){
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

    static Builder builder(@NotNull EntityDamageByEntityEvent event){
        Entity victim = event.getEntity();
        Entity attacker = event.getDamager();
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

    static Builder builder(@NotNull EntityDamageByBlockEvent event){
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

    static Builder builder(@NotNull BlockBreakEvent event){
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

    static Builder builder(@NotNull BlockPlaceEvent event){
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

    static Builder builder(@NotNull PlayerFishEvent event){
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

    @NotNull Random getRandom();
    @Nullable Location getLocation();
    float getLuck();
    @Nullable Object getLooter();
    @Nullable Object getLooted();
    @NotNull
    DataExchange info();

    LootContext withRandom(@NotNull Random random);
    LootContext withLocation(@NotNull Location location);
    LootContext withLooter(@Nullable Object looter);
    LootContext withLooted(@Nullable Object looted);
    LootContext withInfo(@NotNull DataExchange info);

    interface Builder{
        Builder info(DataExchange info);

        Builder random(Random random);

        Builder location(Location location);

        Builder luck(Float luck);
        Builder looter(Object looter);

        Builder looted(Object looted);

        LootContext build();
    }
}
