package killercreepr.crux.core.entity.memory.standard;

import killercreepr.crux.api.communication.boss.ActiveBossBar;
import killercreepr.crux.api.communication.boss.BossBarHolder;
import killercreepr.crux.api.entity.memory.PlayerMemory;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.entity.memory.PlayerTickedDataHolder;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerBossBarHolder extends PlayerTickedDataHolder implements BossBarHolder {
    public static final Key KEY = Crux.key("boss_bar");
    public PlayerBossBarHolder(@NotNull Key key, @NotNull PlayerMemory parent) {
        super(key, parent);
    }
    public PlayerBossBarHolder(@NotNull PlayerMemory parent) {
        this(KEY, parent);
    }

    protected final Map<Key, ActiveBossBar> bars = new ConcurrentHashMap<>();

    @Override
    public boolean shouldRemoveFromMemory(@Nullable Player e) {
        return super.shouldRemoveFromMemory(e) || bars.isEmpty();
    }

    @Override
    protected void onTick(@NotNull Player e) {
        super.onTick(e);
        bars.values().removeIf(active ->{
            active.tick(e);
            if(active.isActive()) return false;
            active.removed(e);
            return true;
        });
    }


    @Override
    public @Nullable ActiveBossBar getBossBar(@NotNull Key key) {
        return bars.get(key);
    }

    @Override
    public boolean addBossBar(@NotNull ActiveBossBar active) {
        return addBossBar(active, false);
    }

    @Override
    public boolean addBossBar(@NotNull ActiveBossBar active, boolean overwrite) {
        if(hasBossBar(active)){
            return false;
        }
        Player p = getPlayerOrThrow();
        removeBossBar(active.key());
        bars.put(active.key(), active);
        active.added(p);
        return true;
    }

    @Override
    public @Nullable ActiveBossBar removeBossBar(@NotNull Key key) {
        ActiveBossBar removed = bars.remove(key);
        if(removed == null) return null;
        Player p = getPlayer();
        if(p == null) return removed;
        removed.removed(p);
        return removed;
    }

    @Override
    public boolean hasBossBar(@NotNull Key key) {
        return bars.containsKey(key);
    }
}
