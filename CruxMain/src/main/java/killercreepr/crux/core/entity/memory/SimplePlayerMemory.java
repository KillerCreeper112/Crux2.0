package killercreepr.crux.core.entity.memory;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.entity.memory.DataHolder;
import killercreepr.crux.api.entity.memory.PlayerMemory;
import killercreepr.crux.api.entity.memory.PlayerTickDataHolder;
import killercreepr.crux.api.entity.memory.TickedDataHolder;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.core.registry.SimpleKeyedRegistry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SimplePlayerMemory extends SimpleEntityMemory implements PlayerMemory {
    protected Long quit = null;
    protected final KeyedRegistry<PlayerDataHolder> playerSpecificHolders = new SimpleKeyedRegistry<>();

    @Override
    public @NotNull KeyedRegistry<PlayerDataHolder> getPlayerSpecificHolders() {
        return playerSpecificHolders;
    }

    protected final Holder<Player> entity;
    protected final PlayerDataHolderRegistry playerDataHolders;

    public SimplePlayerMemory(@NotNull Player e) {
        this(e.getUniqueId(), Holder.weakReference(e));
    }

    public SimplePlayerMemory(@NotNull UUID uuid, @NotNull Holder<Player> player) {
        super(new PlayerDataHolderRegistry(new ConcurrentHashMap<>()), uuid, player);
        this.entity = player;
        this.playerDataHolders = (PlayerDataHolderRegistry) this.dataHolders;
    }

    @Override
    public @Nullable Player value() {
        return entity.value();
    }

    @Override
    public @Nullable Long quit() {
        return quit;
    }
    @Override
    public void scheduleForRemoval(@NotNull Player p) {
        quit = System.currentTimeMillis() + 50L;
        playerDataHolders.forEach(holder -> holder.scheduledRemoval(p));
    }

    @Override
    public boolean shouldRemoveFromMemory(@Nullable Entity e) {
        return quit != null && System.currentTimeMillis() > quit;
    }

    @Override
    public boolean tick() {
        Player e = value();
        removeCount = 0;

        for (TickedDataHolder holder : playerDataHolders.getTickedHolders().values()) {
            if (holder.shouldRemoveFromMemory(e)) {
                holder.removing(e);
                addRemove(holder);
            } else if (e != null) {
                holder.tick(e);
            }
        }
        for (PlayerTickDataHolder holder : playerDataHolders.getPlayerTickedHolders().values()) {
            if (holder.shouldRemoveFromMemory(e)) {
                holder.removing(e);
                addRemove(holder);
            } else if (e != null) {
                holder.tick(e);
            }
        }

        bufferRemove();

        if (shouldRemoveFromMemory(e)) {
            removeDataHolders(e);
            return true;
        }
        return false;
    }
}
