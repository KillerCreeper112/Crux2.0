package killercreepr.crux.api.communication.boss;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BossBarHolder {
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
