package killercreepr.crux.data.entity;

import killercreepr.crux.data.Holder;
import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.crux.registry.SimpleKeyedRegistry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

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
        super(new PlayerDataHolderRegistry(), uuid, player);
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
    public void quit(@Nullable Long time) {
        quit = time;
    }

    @Override
    public boolean shouldRemoveFromMemory(@Nullable Entity e) {
        return quit != null && System.currentTimeMillis() > quit;
    }

    @Override
    public boolean tick() {
        Player e = value();
        playerDataHolders.removeTickedIf(holder -> {
            if (holder.shouldRemoveFromMemory(e)) {
                holder.removing(e);
                return true;
            }
            if (e != null) holder.tick(e);
            return false;
        });

        playerDataHolders.removePlayerTickedIf(holder -> {
            if (holder.shouldRemoveFromMemory(e)) {
                holder.removing(e);
                return true;
            }
            if (e != null) holder.tick(e);
            return false;
        });

        if (shouldRemoveFromMemory(e)) {
            removeDataHolders(e);
            return true;
        }
        return false;
    }
}
