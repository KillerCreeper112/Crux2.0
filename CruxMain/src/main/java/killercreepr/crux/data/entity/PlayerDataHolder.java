package killercreepr.crux.data.entity;

import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PlayerDataHolder extends EntityDataHolder {
    protected final @NotNull PlayerMemory parent;
    public PlayerDataHolder(@NotNull Key key, @NotNull PlayerMemory parent) {
        super(key, parent);
        this.parent = parent;
    }

    protected boolean shouldRemoveFromMemory(@Nullable Player e){ return false; }
    public abstract void tick(@NotNull Player e);

    /**
     * This is not called for PlayerDataHolders
     */
    @Override
    public final void tick(@NotNull Entity e) {
        super.tick(e);
    }

    /**
     * This is not called for PlayerDataHolders
     */
    @Override
    public final boolean shouldRemoveFromMemory(@Nullable Entity e) {
        return super.shouldRemoveFromMemory(e);
    }

    @Override
    public @NotNull PlayerMemory getParent() {
        return parent;
    }

    public @Nullable Player getPlayer(){
        return Bukkit.getPlayer(parent.getUUID());
    }

    public @Nullable OfflinePlayer getOfflinePlayer(){
        return Bukkit.getOfflinePlayer(parent.getUUID());
    }
}
