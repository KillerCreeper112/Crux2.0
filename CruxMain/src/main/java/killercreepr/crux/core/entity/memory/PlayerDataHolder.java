package killercreepr.crux.core.entity.memory;

import killercreepr.crux.api.entity.memory.PlayerMemory;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PlayerDataHolder extends EntityDataHolder {
    protected final @NotNull PlayerMemory parent;
    public PlayerDataHolder(@NotNull Key key, @NotNull PlayerMemory parent) {
        super(key, parent);
        this.parent = parent;
    }

    @Override
    public @NotNull PlayerMemory getParent() {
        return parent;
    }

    public @Nullable Player getPlayer(){
        return parent.value();
    }
    public @NotNull Player getPlayerOrThrow(){
        Player p = parent.value();
        if(p==null) throw new IllegalStateException(parent.getUUID() + " player is not present!");
        return p;
    }

    public @Nullable OfflinePlayer getOfflinePlayer(){
        return Bukkit.getOfflinePlayer(parent.getUUID());
    }
}
