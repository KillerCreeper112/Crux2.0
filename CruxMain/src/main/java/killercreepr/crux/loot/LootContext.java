package killercreepr.crux.loot;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.loot.impl.SimpleLootContext;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public interface LootContext {
    static Builder builder(){
        return new SimpleLootContext.Builder();
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
