package killercreepr.crux.api.communication.boss;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.entity.memory.PlayerMemory;
import killercreepr.crux.core.entity.memory.standard.PlayerBossBarHolder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BossBarHolder {
    static boolean isValidBossBarHolder(@NotNull Audience a){
        return a instanceof Player;
    }

    static @Nullable BossBarHolder getIfPresent(@NotNull Audience a){
        if(!(a instanceof Player p)) return null;
        return EntityMemory.getOrCreateDataHolder(p, PlayerBossBarHolder.class);
    }

    static @Nullable BossBarHolder getOrAdd(@NotNull Audience a){
        if(!(a instanceof Player p)) return null;
        PlayerBossBarHolder holder = EntityMemory.getOrCreateDataHolder(p, PlayerBossBarHolder.class);
        if(holder != null) return holder;
        PlayerMemory mem = PlayerMemory.getOrCreate(p);
        holder = new PlayerBossBarHolder(mem);
        mem.getDataHolders().register(holder);
        return holder;
    }

    @Nullable ActiveBossBar getBossBar(@NotNull Key key);
    boolean addBossBar(@NotNull ActiveBossBar active);
    boolean addBossBar(@NotNull ActiveBossBar active, boolean overwrite);
    default boolean removeBossBar(@NotNull ActiveBossBar active){
        return removeBossBar(active.key()) != null;
    }
    @Nullable ActiveBossBar
    removeBossBar(@NotNull Key key);
    boolean hasBossBar(@NotNull Key key);
    default boolean hasBossBar(@NotNull ActiveBossBar active){
        return hasBossBar(active.key());
    }
}
