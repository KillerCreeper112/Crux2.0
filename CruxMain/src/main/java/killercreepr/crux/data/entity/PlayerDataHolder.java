package killercreepr.crux.data.entity;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PlayerDataHolder extends EntityDataHolder {
    protected final @NotNull PlayerMemory parent;
    public PlayerDataHolder(@NotNull NamespacedKey key, @NotNull PlayerMemory parent) {
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
}
